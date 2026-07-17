package com.rebine.chocostock

import android.app.Application
import com.rebine.chocostock.data.files.ImageStorageManager
import com.rebine.chocostock.data.local.AppDatabase
import com.rebine.chocostock.data.remote.ChocolateAnalysisCoordinator
import com.rebine.chocostock.data.remote.GeminiApiService
import com.rebine.chocostock.data.repository.ChocolateRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class ChocoStockApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    lateinit var repository: ChocolateRepository
        private set

    val imageStorageManager by lazy { ImageStorageManager(this) }
    val geminiApiService by lazy { GeminiApiService(BuildConfig.GEMINI_API_KEY) }

    lateinit var analysisCoordinator: ChocolateAnalysisCoordinator
        private set

    override fun onCreate() {
        super.onCreate()
        val database = AppDatabase.getInstance(this)
        repository = ChocolateRepository(database.chocolateDao())
        analysisCoordinator = ChocolateAnalysisCoordinator(repository, geminiApiService, applicationScope)
    }
}