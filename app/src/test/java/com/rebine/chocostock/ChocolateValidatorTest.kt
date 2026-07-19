package com.rebine.chocostock

import com.rebine.chocostock.domain.ChocolateValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ChocolateValidatorTest {

    @Test
    fun `normal title is valid`() {
        assertTrue(ChocolateValidator.isTitleValid("Lindt Excellence"))
    }

    @Test
    fun `empty title is not valid`() {
        assertFalse(ChocolateValidator.isTitleValid(""))
    }

    @Test
    fun `title with only spaces is not valid`() {
        assertFalse(ChocolateValidator.isTitleValid("   "))
    }

    @Test
    fun `sanitizeTitle removes extra spaces`() {
        assertEquals("Lindt", ChocolateValidator.sanitizeTitle("  Lindt  "))
    }
}