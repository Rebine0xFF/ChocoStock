package com.rebine.chocostock

import android.app.Application
import com.rebine.chocostock.data.files.ImageStorageManager
import com.rebine.chocostock.data.local.AppDatabase
import com.rebine.chocostock.data.repository.ChocolateRepository
import com.rebine.chocostock.data.remote.GeminiApiService
import com.rebine.chocostock.BuildConfig

class ChocoStockApplication : Application() {
    lateinit var repository: ChocolateRepository
        private set

    val imageStorageManager by lazy { ImageStorageManager(this) }
    val geminiApiService by lazy { GeminiApiService(BuildConfig.GEMINI_API_KEY) }

    override fun onCreate() {
        super.onCreate()
        val database = AppDatabase.getInstance(this)
        repository = ChocolateRepository(database.chocolateDao())
    }
}