package com.rebine.chocostock.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.concurrent.TimeUnit

data class ChocolateAnalysisResult(
    val title: String,
    val expiryDateIso: String?
)

class GeminiApiService(private val apiKey: String) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val endpoint =
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.1-flash-lite:generateContent"

    private val prompt = """
        Tu es un assistant qui identifie des produits chocolatés à partir de photos, pour une application de gestion de stock personnel.

        Tu reçois deux images, dans cet ordre :
        1. Le devant de l'emballage d'un produit chocolaté.
        2. Un gros plan sur la date de péremption imprimée sur l'emballage. Cette date peut être écrite dans différents formats (JJ/MM/AAAA, MM/AAAA, ou une mention comme "à consommer de préférence avant fin...").

        À partir de la première image, génère un titre court, synthétique et descriptif du produit : marque + type de produit + variante si elle est visible (par exemple "Lindt Excellence 70% Cacao" ou "Ferrero Rocher, boîte 24 pièces"). Le titre ne doit pas dépasser 60 caractères.

        À partir de la seconde image, lis la date de péremption et retranscris-la strictement au format ISO AAAA-MM-JJ. Si seul le mois est précisé (par exemple "MM/AAAA"), utilise le dernier jour de ce mois. Si la date est illisible, partiellement cachée, ou absente de l'image, renvoie null pour ce champ.

        Règle importante : n'invente jamais une information qui n'est pas clairement visible sur les images. Si la date est illisible, partiellement cachée, ou absente de l'image, renvoie une chaîne vide "" pour ce champ.
    """.trimIndent()

    suspend fun analyzeChocolate(coverPhoto: File, expiryPhoto: File): ChocolateAnalysisResult =
        withContext(Dispatchers.IO) {
            val body = buildRequestBody(coverPhoto, expiryPhoto)

            val request = Request.Builder()
                .url(endpoint)
                .addHeader("x-goog-api-key", apiKey)
                .post(body.toString().toRequestBody("application/json".toMediaType()))
                .build()

            client.newCall(request).execute().use { response ->
                val bodyString = response.body?.string()
                    ?: throw IllegalStateException("Réponse vide de l'API Gemini")

                if (!response.isSuccessful) {
                    throw IllegalStateException("Erreur API Gemini (${response.code}) : $bodyString")
                }

                parseResponse(bodyString)
            }
        }

    private fun compressForUpload(file: File, maxDimension: Int = 1024): ByteArray {
        val options = android.graphics.BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        android.graphics.BitmapFactory.decodeFile(file.absolutePath, options)

        var sampleSize = 1
        while (options.outWidth / sampleSize > maxDimension || options.outHeight / sampleSize > maxDimension) {
            sampleSize *= 2
        }

        val decodeOptions = android.graphics.BitmapFactory.Options().apply {
            inSampleSize = sampleSize
        }
        val bitmap = android.graphics.BitmapFactory.decodeFile(file.absolutePath, decodeOptions)

        val outputStream = java.io.ByteArrayOutputStream()
        bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 85, outputStream)
        bitmap.recycle()

        return outputStream.toByteArray()
    }

    private fun buildRequestBody(coverPhoto: File, expiryPhoto: File): JSONObject {
        val coverBase64 = android.util.Base64.encodeToString(
            compressForUpload(coverPhoto), android.util.Base64.NO_WRAP
        )
        val expiryBase64 = android.util.Base64.encodeToString(
            compressForUpload(expiryPhoto), android.util.Base64.NO_WRAP
        )

        val parts = JSONArray().apply {
            put(JSONObject().put("text", prompt))
            put(JSONObject().put(
                "inline_data",
                JSONObject().put("mime_type", "image/jpeg").put("data", coverBase64)
            ))
            put(JSONObject().put(
                "inline_data",
                JSONObject().put("mime_type", "image/jpeg").put("data", expiryBase64)
            ))
        }

        val schema = JSONObject().apply {
            put("type", "object")
            put("properties", JSONObject().apply {
                put("titre", JSONObject().apply {
                    put("type", "string")
                    put("description", "Titre court et descriptif du produit chocolaté (max 60 caractères)")
                })
                put("date_peremption", JSONObject().apply {
                    put("type", "string")
                    put("description", "Date de péremption au format AAAA-MM-JJ, ou chaîne vide si illisible")
                })
            })
            put("required", JSONArray(listOf("titre", "date_peremption")))
        }

        return JSONObject().apply {
            put("contents", JSONArray().put(JSONObject().put("parts", parts)))
            put("generationConfig", JSONObject().apply {
                put("response_mime_type", "application/json")
                put("response_schema", schema)
                put("thinkingConfig", JSONObject().put("thinkingLevel", "minimal"))
            })
        }
    }

    private fun parseResponse(bodyString: String): ChocolateAnalysisResult {
        val root = JSONObject(bodyString)
        val text = root
            .getJSONArray("candidates")
            .getJSONObject(0)
            .getJSONObject("content")
            .getJSONArray("parts")
            .getJSONObject(0)
            .getString("text")

        val parsed = JSONObject(text)
        return ChocolateAnalysisResult(
            title = parsed.getString("titre"),
            expiryDateIso = parsed.getString("date_peremption").ifBlank { null }
        )
    }
}