package com.rebine.chocostock

import android.app.Application
import com.rebine.chocostock.data.local.AppDatabase
import com.rebine.chocostock.data.repository.ChocolateRepository

class ChocoStockApplication : Application() {
    lateinit var repository: ChocolateRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val database = AppDatabase.getInstance(this)
        repository = ChocolateRepository(database.chocolateDao())
    }
}