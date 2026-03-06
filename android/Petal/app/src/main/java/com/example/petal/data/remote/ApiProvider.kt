package com.example.petal.data.remote

import android.content.Context
import com.example.petal.MemoryRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.runBlocking
import com.example.petal.ui.Auth.AuthApi
import com.example.petal.ui.Auth.AuthInterceptor
import com.example.petal.ui.Auth.AuthRepository
import com.example.petal.ui.Auth.TokenManager

object ApiProvider {

    private lateinit var tokenManager: TokenManager
    private lateinit var retrofit: Retrofit

    private val tokenProvider: () -> String? = {
        runBlocking { tokenManager.getAccessToken() }
    }

    fun initialize(context: Context) {
        tokenManager = TokenManager(context)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenProvider))
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl("https://petal-production.up.railway.app/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    val memoryApi: MemoryApi by lazy {
        retrofit.create(MemoryApi::class.java)
    }

    val memoryRepository: MemoryRepository by lazy {
        MemoryRepository(memoryApi)
    }
    val authRepository: AuthRepository by lazy {
        AuthRepository(authApi, tokenManager)
    }
}