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
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"

    private val prompt = """
        Tu es un assistant qui identifie des produits chocolatés à partir de photos, pour une application de gestion de stock personnel.

        Tu reçois deux images, dans cet ordre :
        1. Le devant de l'emballage d'un produit chocolaté.
        2. Un gros plan sur la date de péremption imprimée sur l'emballage. Cette date peut être écrite dans différents formats (JJ/MM/AAAA, MM/AAAA, ou une mention comme "à consommer de préférence avant fin...").

        À partir de la première image, génère un titre court, synthétique et descriptif du produit : marque + type de produit + variante si elle est visible (par exemple "Lindt Excellence 70% Cacao" ou "Ferrero Rocher, boîte 24 pièces"). Le titre ne doit pas dépasser 60 caractères.

        À partir de la seconde image, lis la date de péremption et retranscris-la strictement au format ISO AAAA-MM-JJ. Si seul le mois est précisé (par exemple "MM/AAAA"), utilise le dernier jour de ce mois. Si la date est illisible, partiellement cachée, ou absente de l'image, renvoie null pour ce champ.

        Règle importante : n'invente jamais une information qui n'est pas clairement visible sur les images. En cas de doute, préfère renvoyer une valeur nulle ou approximative plutôt qu'une valeur inventée.
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

    private fun buildRequestBody(coverPhoto: File, expiryPhoto: File): JSONObject {
        val coverBase64 = android.util.Base64.encodeToString(
            coverPhoto.readBytes(), android.util.Base64.NO_WRAP
        )
        val expiryBase64 = android.util.Base64.encodeToString(
            expiryPhoto.readBytes(), android.util.Base64.NO_WRAP
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
                    put("type", JSONArray(listOf("string", "null")))
                    put("description", "Date de péremption au format AAAA-MM-JJ, ou null si illisible")
                })
            })
            put("required", JSONArray(listOf("titre", "date_peremption")))
        }

        val responseFormat = JSONObject().put(
            "text", JSONObject()
                .put("mimeType", "application/json")
                .put("schema", schema)
        )

        return JSONObject().apply {
            put("contents", JSONArray().put(JSONObject().put("parts", parts)))
            put("generationConfig", JSONObject().put("responseFormat", responseFormat))
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
            expiryDateIso = if (parsed.isNull("date_peremption")) null else parsed.getString("date_peremption")
        )
    }
}