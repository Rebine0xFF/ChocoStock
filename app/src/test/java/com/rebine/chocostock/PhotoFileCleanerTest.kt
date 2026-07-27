package com.rebine.chocostock

import com.rebine.chocostock.data.files.PhotoFileCleaner
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class PhotoFileCleanerTest {

    @Test
    fun `deleteIfExists deletes an existing file`() {
        val tempFile = File.createTempFile("chocostock_test", ".jpg")
        assertTrue(tempFile.exists())

        PhotoFileCleaner.deleteIfExists(tempFile.absolutePath)

        assertFalse(tempFile.exists())
    }

    @Test
    fun `deleteIfExists does nothing with null path`() {
        PhotoFileCleaner.deleteIfExists(null) // should not throw exception
    }

    @Test
    fun `deleteIfExists does nothing with empty path`() {
        PhotoFileCleaner.deleteIfExists("   ") // should not throw exception
    }

    @Test
    fun `deleteIfExists does nothing if file does not exist`() {
        PhotoFileCleaner.deleteIfExists("/non/existent/path/file.jpg") // should not throw exception
    }
}