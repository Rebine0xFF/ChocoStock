package com.rebine.chocostock.domain

object ChocolateValidator {
    fun isTitleValid(title: String): Boolean = title.trim().isNotEmpty()
    fun sanitizeTitle(title: String): String = title.trim()
}