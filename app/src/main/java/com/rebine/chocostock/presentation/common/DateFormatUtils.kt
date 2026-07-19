package com.rebine.chocostock.presentation.common

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateFormatUtils {

    private val displayFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH)
    private val editFormatter = DateTimeFormatter.ofPattern("dd/MM/yy", Locale.FRENCH)

    /** Ex : "1 janvier 2026", ou "Date inconnue" */
    fun formatForDisplay(iso: String?): String {
        val date = parseIsoOrNull(iso) ?: return "Date inconnue"
        return date.format(displayFormatter)
    }

    /** Ex : "01/01/26", ou chaîne vide */
    fun formatForEditField(iso: String?): String {
        val date = parseIsoOrNull(iso) ?: return ""
        return date.format(editFormatter)
    }

    fun parseIsoOrNull(iso: String?): LocalDate? {
        if (iso.isNullOrBlank()) return null
        return try {
            LocalDate.parse(iso) // ISO format AAAA-MM-JJ
        } catch (e: Exception) {
            null
        }
    }
}