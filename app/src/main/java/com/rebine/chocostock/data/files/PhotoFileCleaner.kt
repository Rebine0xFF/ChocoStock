package com.rebine.chocostock.data.files

import java.io.File

object PhotoFileCleaner {
    fun deleteIfExists(path: String?) {
        if (path.isNullOrBlank()) return
        val file = File(path)
        if (file.exists()) file.delete()
    }
}