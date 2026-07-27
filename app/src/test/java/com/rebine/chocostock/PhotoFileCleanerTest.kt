package com.rebine.chocostock

import com.rebine.chocostock.data.files.PhotoFileCleaner
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class PhotoFileCleanerTest {

    @Test
    fun `deleteIfExists supprime un fichier existant`() {
        val tempFile = File.createTempFile("chocostock_test", ".jpg")
        assertTrue(tempFile.exists())

        PhotoFileCleaner.deleteIfExists(tempFile.absolutePath)

        assertFalse(tempFile.exists())
    }

    @Test
    fun `deleteIfExists ne fait rien avec un chemin null`() {
        PhotoFileCleaner.deleteIfExists(null) // ne doit pas lever d'exception
    }

    @Test
    fun `deleteIfExists ne fait rien avec un chemin vide`() {
        PhotoFileCleaner.deleteIfExists("   ") // ne doit pas lever d'exception
    }

    @Test
    fun `deleteIfExists ne fait rien si le fichier n'existe pas`() {
        PhotoFileCleaner.deleteIfExists("/chemin/inexistant/fichier.jpg") // ne doit pas lever d'exception
    }
}