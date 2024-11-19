package com.example.pistagram.reelpage

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException

class ReelFetcher {
    private val client = OkHttpClient()
    private val mainHandler = Handler(Looper.getMainLooper())

    fun fetchReels(page: Int, callback: (List<Reel>?) -> Unit) {
        val apiUrl = "https://share.myjosh.in/webview/apiwbody"
        val requestBody = createRequestBody(page)
        val request = createRequest(apiUrl, requestBody)

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                e.printStackTrace()
                runOnMainThread { callback(null) }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                response.use {
                    if (!response.isSuccessful || response.body == null) {
                        runOnMainThread { callback(null) }
                        return
                    }
                    val reels = parseResponse(response.body!!.string())
                    runOnMainThread { callback(reels) }
                }
            }
        })
    }

    private fun createRequestBody(page: Int): RequestBody {
        val jnurl = "https://feed-internal.coolfie.io/feed/latest?lang_code=en&page=$page&rows=10"
        val requestBodyJson = JSONObject().apply {
            put("method", "POST")
            put("url", jnurl)
            put("body", JSONObject())
            put("platform", "PWA")
        }
        return RequestBody.create("application/json".toMediaType(), requestBodyJson.toString())
    }

    private fun createRequest(apiUrl: String, requestBody: RequestBody): Request {
        return Request.Builder()
            .url(apiUrl)
            .post(requestBody)
            .addHeader("content-type", "application/json")
            .build()
    }

    private fun parseResponse(responseBody: String): List<Reel>? {
        val jsonResponse = JSONObject(responseBody)
        val reelsData = jsonResponse.optJSONArray("data") ?: return null

        return List(reelsData.length()) { index ->
            val reelJson = reelsData.getJSONObject(index)
            Reel(
                reelId = reelJson.optString("content_uuid"),
                reelDescription = reelJson.optString("content_title"),
                videoUrl = reelJson.optString("ucq_url"),
                trackAvatar = reelJson.optJSONObject("audio_track_meta")?.optString("album_art"),
                trackName = reelJson.optJSONObject("audio_track_meta")?.optString("title"),
                username = reelJson.optJSONObject("user_profile")?.optString("name"),
                likesCount = reelJson.optInt("like_count", 0),
                commentsCount = reelJson.optInt("comments_count", 0),
                sharesCount = reelJson.optInt("share_count", 0)
            )
        }
    }

    private fun runOnMainThread(action: () -> Unit) {
        mainHandler.post(action)
    }
}
