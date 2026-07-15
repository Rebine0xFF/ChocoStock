package com.rebine.chocostock.presentation.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rebine.chocostock.data.repository.ChocolateRepository
import com.rebine.chocostock.domain.model.Chocolate
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

class AddChocolateViewModel(private val repository: ChocolateRepository) : ViewModel() {

    fun saveChocolate(coverPhoto: File?, expiryPhoto: File?, onDone: () -> Unit) {
        viewModelScope.launch {
            repository.addChocolate(
                Chocolate(
                    id = UUID.randomUUID().toString(),
                    title = "Nouveau chocolat", // AI fill later on
                    coverImagePath = coverPhoto?.absolutePath,
                    expiryPhotoPath = expiryPhoto?.absolutePath,
                    expiryDateIso = null, // AI fill later on
                    dateAdded = System.currentTimeMillis()
                )
            )
            onDone()
        }
    }
}

class AddChocolateViewModelFactory(private val repository: ChocolateRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        AddChocolateViewModel(repository) as T
}