package com.rebine.chocostock.domain.model

data class Chocolate(
    val id: String,
    val title: String,
    val coverImagePath: String?,
    val expiryPhotoPath: String?,
    val expiryDateIso: String?,
    val dateAdded: Long
)