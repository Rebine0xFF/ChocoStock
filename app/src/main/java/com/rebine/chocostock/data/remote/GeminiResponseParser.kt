package com.rebine.chocostock.data.remote

import org.json.JSONObject

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
}