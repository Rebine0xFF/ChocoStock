package com.rebine.chocostock.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chocolates")
data class ChocolateEntity(
    @PrimaryKey val id: String,
    val title: String,
    val expiryDateIso: String?,
    val dateAdded: Long
)