package com.example.petal.data.remote

import android.content.Context
import com.example.petal.MemoryRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.runBlocking
import com.example.petal.ui.auth.AuthApi
import com.example.petal.ui.auth.AuthInterceptor
import com.example.petal.ui.auth.AuthRepository
import com.example.petal.ui.auth.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

object ApiProvider {

    private lateinit var tokenManager: TokenManager
    private lateinit var retrofit: Retrofit


    var onUnauthorized: (() -> Unit)? = null
    private val tokenProvider: () -> String? = {
        runBlocking { tokenManager.getAccessToken() }
    }

    private val retryInterceptor = Interceptor { chain ->
        val request = chain.request()
        var lastException: Exception? = null

        repeat(3) { attempt ->
            try {
                val response = chain.proceed(request.newBuilder().build())
                if (response.isSuccessful || response.code == 401 || response.code == 400) {
                    return@Interceptor response
                }
                response.close()
            } catch (e: Exception) {
                   lastException = e
                if (attempt < 2) Thread.sleep(3000)
            }
        }
        throw (lastException ?: Exception("Failed after 3 attempts"))
    }

    private val unauthorizedInterceptor = Interceptor { chain ->
        val response = chain.proceed(chain.request())
            if (response.code == 401) {
            runBlocking { tokenManager.clearTokens() }
            onUnauthorized?.invoke()
        }
        response
    }

    fun initialize(context: Context) {
        tokenManager = TokenManager(context)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(retryInterceptor)
            .addInterceptor(AuthInterceptor(tokenProvider))
            .addInterceptor(unauthorizedInterceptor)
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl("https://petal-lr5s.onrender.com/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
    val memoryApi: MemoryApi by lazy { retrofit.create(MemoryApi::class.java) }
    val memoryRepository: MemoryRepository by lazy { MemoryRepository(memoryApi) }
    val authRepository: AuthRepository by lazy { AuthRepository(authApi, tokenManager) }
}