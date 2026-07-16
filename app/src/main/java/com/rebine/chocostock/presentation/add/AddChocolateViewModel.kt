package com.rebine.chocostock.presentation.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rebine.chocostock.data.remote.GeminiApiService
import com.rebine.chocostock.data.repository.ChocolateRepository
import com.rebine.chocostock.domain.model.Chocolate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

sealed class AnalysisState {
    object Idle : AnalysisState()
    object Loading : AnalysisState()
    data class Success(val title: String, val expiryDateIso: String?) : AnalysisState()
    data class Error(val message: String) : AnalysisState()
}

class AddChocolateViewModel(
    private val repository: ChocolateRepository,
    private val geminiApiService: GeminiApiService
) : ViewModel() {

    private val _analysisState = MutableStateFlow<AnalysisState>(AnalysisState.Idle)
    val analysisState: StateFlow<AnalysisState> = _analysisState.asStateFlow()

    fun analyzePhotos(coverPhoto: File, expiryPhoto: File) {
        _analysisState.value = AnalysisState.Loading
        viewModelScope.launch {
            try {
                val result = geminiApiService.analyzeChocolate(coverPhoto, expiryPhoto)
                _analysisState.value = AnalysisState.Success(result.title, result.expiryDateIso)
            } catch (e: Exception) {
                _analysisState.value = AnalysisState.Error(e.message ?: "Erreur inconnue")
            }
        }
    }

    fun saveChocolate(
        title: String,
        expiryDateIso: String?,
        coverPhoto: File?,
        expiryPhoto: File?,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            repository.addChocolate(
                Chocolate(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    coverImagePath = coverPhoto?.absolutePath,
                    expiryPhotoPath = expiryPhoto?.absolutePath,
                    expiryDateIso = expiryDateIso,
                    dateAdded = System.currentTimeMillis()
                )
            )
            onDone()
        }
    }
}

class AddChocolateViewModelFactory(
    private val repository: ChocolateRepository,
    private val geminiApiService: GeminiApiService
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        AddChocolateViewModel(repository, geminiApiService) as T
}