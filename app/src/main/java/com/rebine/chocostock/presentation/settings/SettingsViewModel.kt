package com.rebine.chocostock.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rebine.chocostock.data.local.ApiKeyRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val apiKeyRepository: ApiKeyRepository) : ViewModel() {

    val apiKey: StateFlow<String?> = apiKeyRepository.apiKeyFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun saveApiKey(key: String) {
        viewModelScope.launch { apiKeyRepository.saveApiKey(key.trim()) }
    }
}

class SettingsViewModelFactory(private val apiKeyRepository: ApiKeyRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = SettingsViewModel(apiKeyRepository) as T
}