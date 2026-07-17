package com.rebine.chocostock.presentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rebine.chocostock.data.repository.ChocolateRepository
import com.rebine.chocostock.domain.model.Chocolate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditChocolateViewModel(private val repository: ChocolateRepository) : ViewModel() {

    private val _chocolate = MutableStateFlow<Chocolate?>(null)
    val chocolate: StateFlow<Chocolate?> = _chocolate.asStateFlow()

    fun load(id: String) {
        viewModelScope.launch {
            _chocolate.value = repository.getById(id)
        }
    }

    fun save(title: String, expiryDateIso: String?, onDone: () -> Unit) {
        val current = _chocolate.value ?: return
        viewModelScope.launch {
            repository.addChocolate(
                current.copy(
                    title = title,
                    expiryDateIso = expiryDateIso,
                    isAnalyzing = false,
                    analysisFailed = false
                )
            )
            onDone()
        }
    }
}

class EditChocolateViewModelFactory(
    private val repository: ChocolateRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        EditChocolateViewModel(repository) as T
}