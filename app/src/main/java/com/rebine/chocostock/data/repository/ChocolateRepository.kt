package com.rebine.chocostock.data.repository

import com.rebine.chocostock.data.local.ChocolateDao
import com.rebine.chocostock.data.local.ChocolateEntity
import com.rebine.chocostock.domain.model.Chocolate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChocolateRepository(private val dao: ChocolateDao) {

    fun getAllSortedByExpiry(): Flow<List<Chocolate>> =
        dao.getAllSortedByExpiry().map { list -> list.map { it.toDomain() } }

    suspend fun addChocolate(chocolate: Chocolate) {
        dao.insert(chocolate.toEntity())
    }

    suspend fun removeChocolate(chocolate: Chocolate) {
        dao.delete(chocolate.toEntity())
    }
}

private fun ChocolateEntity.toDomain() =
    Chocolate(id, title, coverImagePath, expiryPhotoPath, expiryDateIso, dateAdded)

private fun Chocolate.toEntity() =
    ChocolateEntity(id, title, coverImagePath, expiryPhotoPath, expiryDateIso, dateAdded)