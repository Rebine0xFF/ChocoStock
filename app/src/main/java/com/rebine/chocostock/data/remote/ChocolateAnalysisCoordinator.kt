package com.rebine.chocostock.data.remote

import com.rebine.chocostock.data.repository.ChocolateRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

class ChocolateAnalysisCoordinator(
    private val repository: ChocolateRepository,
    private val geminiApiService: GeminiApiService,
    private val scope: CoroutineScope
) {
    fun analyze(chocolateId: String, coverPhoto: File, expiryPhoto: File) {
        scope.launch {
            val current = repository.getById(chocolateId) ?: return@launch

            val updated = try {
                val result = geminiApiService.analyzeChocolate(coverPhoto, expiryPhoto)
                current.copy(
                    title = result.title,
                    expiryDateIso = result.expiryDateIso,
                    isAnalyzing = false,
                    analysisFailed = false
                )
            } catch (e: Exception) {
                android.util.Log.e("ChocoStock", "Erreur analyse Gemini", e)
                current.copy(
                    title = "Analyse échouée, modifie les infos",
                    isAnalyzing = false,
                    analysisFailed = true
                )
            }

            repository.addChocolate(updated) // REPLACE : met à jour l'entrée existante
        }
    }
}