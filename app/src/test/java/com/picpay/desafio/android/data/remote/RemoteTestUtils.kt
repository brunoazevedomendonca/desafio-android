package com.picpay.desafio.android.data.remote

import okio.buffer
import okio.source

class RemoteTestUtils {
    companion object {
        fun getJsonFileString(fileName: String): String {
            val inputStream = this::class.java.classLoader!!.getResourceAsStream(fileName)
            val source = inputStream.source().buffer()
            val result = source.readString(Charsets.UTF_8)
            source.close()
            inputStream.close()
            return result
        }
    }
}