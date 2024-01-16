package com.nizarfadlan.uasc_nizar.utils

import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request

class Request(private val client: OkHttpClient) {

    fun get(url: String, callback: Callback): Call {
        val request = Request.Builder()
            .url(url)
            .header("Content-Type", JSON.toString())
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    companion object {
        val JSON = MediaType.parse("application/json; charset=utf-8")
    }
}