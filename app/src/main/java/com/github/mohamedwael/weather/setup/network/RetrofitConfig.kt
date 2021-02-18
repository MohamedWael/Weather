package com.github.mohamedwael.weather.setup.network

import com.github.mohamedwael.weather.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val INITIALIZATION_EXCEPTION_MESSAGE =
        "You have to initialize RetrofitConfig config first. Use the RetrofitConfig.Builder to initialize it"

class RetrofitConfig private constructor() {

    var logToggle = BuildConfig.DEBUG
    var httpClient: OkHttpClient.Builder? = null
    var retrofit: Retrofit? = null

    class Builder {
        private var url: String? = null
        private var httpClient: OkHttpClient.Builder? = null
        private var logToggle: Boolean? = null
        private val interceptors by lazy { mutableListOf<Interceptor>() }

        fun url(url: String): Builder {
            this.url = url
            return this
        }

        /**
         * Logger is enabled by default in the debug mode and closed in release
         */
        fun enableLogger(logToggle: Boolean?): Builder {
            this.logToggle = logToggle
            return this
        }

        fun httpClientBuilder(httpClient: OkHttpClient.Builder): Builder {
            this.httpClient = httpClient
            return this
        }

        internal fun addInterceptor(interceptor: Interceptor): Builder {
            interceptors.add(interceptor)
            return this
        }

        fun build(): RetrofitConfig {
            val config = RetrofitConfig()
            config.logToggle = logToggle ?: BuildConfig.DEBUG
            val httpClient = httpClient ?: OkHttpClient.Builder()
            interceptors.forEach {
                if (!httpClient.interceptors().contains(it)) {
                    httpClient.addInterceptor(it)
                }
            }
            config.init(
                    httpClient, checkNotNull(url) { "url can't be null" }, GsonConverterFactory.create()
            )
            return config
        }
    }

    private fun init(httpClient: OkHttpClient.Builder, baseUrl: String, factory: Converter.Factory) {
        this.httpClient = httpClient
        setUpLogger()
        val builder = Retrofit.Builder().baseUrl(if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/")
                .addConverterFactory(factory)
        retrofit = builder.client(httpClient.build()).build()
    }

    fun addInterceptor(interceptor: Interceptor) {
        checkNotNull(httpClient) { INITIALIZATION_EXCEPTION_MESSAGE }.addInterceptor(interceptor)
    }

    fun <T> createClient(tClass: Class<T>): T {
        return checkNotNull(retrofit) { INITIALIZATION_EXCEPTION_MESSAGE }.create(tClass)
    }

    private fun setUpLogger() {
        if (httpClient != null && logToggle) {
            httpClient!!.addInterceptor(newBodyLogger())
            httpClient!!.addInterceptor(newHeaderLogger())
        }
    }

    private fun newHeaderLogger(): Interceptor {
        return newLogger(HttpLoggingInterceptor.Level.HEADERS)
    }

    private fun newBodyLogger(): HttpLoggingInterceptor {
        return newLogger(HttpLoggingInterceptor.Level.BODY)
    }

    private fun newLogger(level: HttpLoggingInterceptor.Level): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = level
        return loggingInterceptor
    }
}
