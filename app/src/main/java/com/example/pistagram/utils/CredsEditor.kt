package com.example.pistagram.utils

import android.content.Context
import java.io.File
import java.io.FileWriter

class CredsEditor {
    fun writeFileOnInternalStorage(mcoContext: Context, sFileName: String?, sBody: String?) {
        val dir: File = File(mcoContext.filesDir, "credentials")
        if (!dir.exists()) {
            dir.mkdir()
        }

        try {
            val gpxfile = sFileName?.let { File(dir, it) }
            val writer = FileWriter(gpxfile)
            writer.append(sBody)
            writer.flush()
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun readFileFromInternalStorage(mcoContext: Context, sFileName: String?): String? {
        val dir: File = File(mcoContext.filesDir, "credentials")
        val gpxfile = sFileName?.let { File(dir, it) }
        if (gpxfile!!.exists()) {
            val contents = gpxfile.readText()
            return contents
        } else {
            return null
        }
    }

}