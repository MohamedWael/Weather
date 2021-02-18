package com.github.mohamedwael.weather.setup.network

import okhttp3.Interceptor
import okhttp3.Request

const val API_KEY = "d07d1a50997df598b42e5a16aec834a2"

fun getApiInterceptor() = Interceptor {
    val original = it.request()
    val originalHttpUrl = original.url
    val url = originalHttpUrl.newBuilder()
        .addQueryParameter("appid", API_KEY).build()

    val requestBuilder: Request.Builder = original.newBuilder()
        .url(url)
    it.proceed(requestBuilder.build());
}
