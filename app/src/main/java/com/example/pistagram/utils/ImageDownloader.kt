package com.example.pistagram.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class ImageDownloader {
    suspend fun downloadImageFromUrl(url_: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(url_)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val inputStream = connection.inputStream
                BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                Log.d("Image", e.stackTraceToString())
                null
            }
        }
    }
}
