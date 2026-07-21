package com.rebine.chocostock.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ChocolateEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract fun chocolateDao(): ChocolateDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "chocostock.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}