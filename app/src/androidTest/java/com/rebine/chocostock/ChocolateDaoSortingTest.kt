package com.rebine.chocostock

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rebine.chocostock.data.local.AppDatabase
import com.rebine.chocostock.data.local.ChocolateEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChocolateDaoSortingTest {

    private lateinit var database: AppDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun sorting_places_the_closest_date_first_and_unknown_dates_at_the_end() = runBlocking {
        val dao = database.chocolateDao()

        dao.insert(entity(id = "1", title = "Sans date", expiryDateIso = null))
        dao.insert(entity(id = "2", title = "Le plus lointain", expiryDateIso = "2027-01-01"))
        dao.insert(entity(id = "3", title = "Le plus proche", expiryDateIso = "2026-08-01"))

        val result = dao.getAllSortedByExpiry().first()

        assertEquals(listOf("Le plus proche", "Le plus lointain", "Sans date"), result.map { it.title })
    }

    private fun entity(id: String, title: String, expiryDateIso: String?) = ChocolateEntity(
        id = id,
        title = title,
        coverImagePath = null,
        expiryPhotoPath = null,
        expiryDateIso = expiryDateIso,
        dateAdded = System.currentTimeMillis()
    )
}