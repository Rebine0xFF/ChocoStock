package com.rebine.chocostock

import com.rebine.chocostock.presentation.common.DateFormatUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class DateFormatUtilsTest {

    @Test
    fun `formatForDisplay with a valid date returns the long French format`() {
        val result = DateFormatUtils.formatForDisplay("2026-01-01")
        assertEquals("1 janvier 2026", result)
    }

    @Test
    fun `formatForDisplay with null returns Date inconnue`() {
        assertEquals("Date inconnue", DateFormatUtils.formatForDisplay(null))
    }

    @Test
    fun `formatForDisplay with an invalid string returns Date inconnue`() {
        assertEquals("Date inconnue", DateFormatUtils.formatForDisplay("pas une date"))
    }

    @Test
    fun `formatForEditField with a valid date returns the short format`() {
        val result = DateFormatUtils.formatForEditField("2026-01-01")
        assertEquals("01/01/26", result)
    }

    @Test
    fun `parseIsoOrNull with an empty string returns null`() {
        assertEquals(null, DateFormatUtils.parseIsoOrNull(""))
    }
}