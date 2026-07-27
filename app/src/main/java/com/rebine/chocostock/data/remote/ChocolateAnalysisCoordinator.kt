package com.rebine.chocostock.data.remote

import com.rebine.chocostock.data.local.ApiKeyRepository
import com.rebine.chocostock.data.repository.ChocolateRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

class ChocolateAnalysisCoordinator(
    private val repository: ChocolateRepository,
    private val geminiApiService: GeminiApiService,
    private val apiKeyRepository: ApiKeyRepository,
    private val scope: CoroutineScope
) {
    fun analyze(chocolateId: String, coverPhoto: File, expiryPhoto: File) {
        scope.launch {
            val current = repository.getById(chocolateId) ?: return@launch
            val apiKey = apiKeyRepository.apiKeyFlow.first()

            val updated = try {
                if (apiKey.isNullOrBlank()) {
                    throw MissingApiKeyException("Aucune clé API Gemini renseignée.")
                }
                val result = geminiApiService.analyzeChocolate(coverPhoto, expiryPhoto, apiKey)
                current.copy(
                    title = result.title,
                    expiryDateIso = result.expiryDateIso,
                    isAnalyzing = false,
                    analysisFailed = false
                )
            } catch (e: MissingApiKeyException) {
                android.util.Log.e("ChocoStock", "Clé API manquante", e)
                current.copy(title = "Aucune clé API, va dans Réglages", isAnalyzing = false, analysisFailed = true)
            } catch (e: InvalidApiKeyException) {
                android.util.Log.e("ChocoStock", "Clé API invalide", e)
                current.copy(title = "Clé API invalide, vérifie Réglages", isAnalyzing = false, analysisFailed = true)
            } catch (e: Exception) {
                android.util.Log.e("ChocoStock", "Erreur analyse Gemini", e)
                current.copy(title = "Analyse échouée, modifie les infos", isAnalyzing = false, analysisFailed = true)
            }
            repository.addChocolate(updated)
        }
    }
}