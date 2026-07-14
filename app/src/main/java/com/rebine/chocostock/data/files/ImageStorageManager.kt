package com.rebine.chocostock.data.files

import android.content.Context
import java.io.File
import java.util.UUID

class ImageStorageManager(private val context: Context) {

    fun createImageFile(): File {
        val dir = File(context.filesDir, "chocolate_photos").apply { mkdirs() }
        return File(dir, "${UUID.randomUUID()}.jpg")
    }
}