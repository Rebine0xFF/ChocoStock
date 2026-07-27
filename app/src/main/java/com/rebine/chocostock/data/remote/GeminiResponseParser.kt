package com.rebine.chocostock.data.remote

import org.json.JSONObject

class MissingApiKeyException(message: String) : Exception(message)
class InvalidApiKeyException(message: String) : Exception(message)
class GeminiApiException(message: String) : Exception(message)
object GeminiResponseParser {

    fun parse(bodyString: String): ChocolateAnalysisResult {
        val root = JSONObject(bodyString)
        val candidates = root.optJSONArray("candidates")
            ?: throw IllegalStateException("Réponse Gemini sans candidat : $bodyString")

        if (candidates.length() == 0) {
            throw IllegalStateException("Aucun candidat renvoyé par Gemini : $bodyString")
        }

        val text = candidates.getJSONObject(0)
            .getJSONObject("content")
            .getJSONArray("parts")
            .getJSONObject(0)
            .getString("text")

        val parsed = JSONObject(text)
        val title = parsed.optString("titre").ifBlank { "Chocolat sans nom" }
        val expiryDateIso = parsed.optString("date_peremption").ifBlank { null }

        return ChocolateAnalysisResult(title = title, expiryDateIso = expiryDateIso)
    }

    fun throwForErrorResponse(httpCode: Int, bodyString: String): Nothing {
        val errorObject = try {
            JSONObject(bodyString).optJSONObject("error")
        } catch (e: Exception) {
            null
        }
        val message = errorObject?.optString("message")?.ifBlank { null } ?: bodyString

        val looksLikeInvalidKey =
            message.contains("API key not valid", ignoreCase = true) ||
                    message.contains("API_KEY_INVALID", ignoreCase = true) ||
                    httpCode == 403

        if (looksLikeInvalidKey) {
            throw InvalidApiKeyException("Clé API invalide ou refusée par Gemini : $message")
        }
        throw GeminiApiException("Erreur API Gemini ($httpCode) : $message")
    }
}