package com.rebine.chocostock.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rebine.chocostock.data.repository.ChocolateRepository
import com.rebine.chocostock.domain.model.Chocolate
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class ChocolateListViewModel(private val repository: ChocolateRepository) : ViewModel() {

    val chocolates: StateFlow<List<Chocolate>> = repository.getAllSortedByExpiry()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // TEMP : FOR TESTING ONLY
    fun addFakeChocolate() {
        val fakes = listOf(
            Chocolate(UUID.randomUUID().toString(), "Tablette Lindt Excellence 70%", "2026-09-15", System.currentTimeMillis()),
            Chocolate(UUID.randomUUID().toString(), "Ferrero Rocher (boîte)", "2026-08-01", System.currentTimeMillis()),
            Chocolate(UUID.randomUUID().toString(), "Chocolat artisanal noisette", null, System.currentTimeMillis()),
            Chocolate(UUID.randomUUID().toString(), "Tablette Milka Alpenmilch", "2026-07-30", System.currentTimeMillis())
        )
        viewModelScope.launch {
            repository.addChocolate(fakes.random())
        }
    }
}

class ChocolateListViewModelFactory(private val repository: ChocolateRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChocolateListViewModel(repository) as T
    }
}