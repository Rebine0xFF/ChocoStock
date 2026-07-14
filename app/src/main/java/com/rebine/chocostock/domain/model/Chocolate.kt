package com.rebine.chocostock.domain.model

data class Chocolate(
    val id: String,
    val title: String,
    val expiryDateIso: String?, // "AAAA-MM-JJ", or null if unknown
    val dateAdded: Long
)