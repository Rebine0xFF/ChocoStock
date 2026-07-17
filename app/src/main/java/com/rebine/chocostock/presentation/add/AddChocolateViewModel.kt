package com.rebine.chocostock.presentation.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rebine.chocostock.data.remote.ChocolateAnalysisCoordinator
import com.rebine.chocostock.data.repository.ChocolateRepository
import com.rebine.chocostock.domain.model.Chocolate
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

class AddChocolateViewModel(
    private val repository: ChocolateRepository,
    private val analysisCoordinator: ChocolateAnalysisCoordinator
) : ViewModel() {

    fun createChocolateAndAnalyze(coverPhoto: File, expiryPhoto: File) {
        viewModelScope.launch {
            val id = UUID.randomUUID().toString()
            repository.addChocolate(
                Chocolate(
                    id = id,
                    title = "Analyse en cours...",
                    coverImagePath = coverPhoto.absolutePath,
                    expiryPhotoPath = expiryPhoto.absolutePath,
                    expiryDateIso = null,
                    dateAdded = System.currentTimeMillis(),
                    isAnalyzing = true,
                    analysisFailed = false
                )
            )
            // Launched on the application scope: continues even if we leave this screen
            analysisCoordinator.analyze(id, coverPhoto, expiryPhoto)
        }
    }
}

class AddChocolateViewModelFactory(
    private val repository: ChocolateRepository,
    private val analysisCoordinator: ChocolateAnalysisCoordinator
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        AddChocolateViewModel(repository, analysisCoordinator) as T
}