package com.example.petal.ui.auth

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenProvider: () -> String?
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val path = request.url.encodedPath

        if (path.contains("/login") || path.contains("/register")) {
            return chain.proceed(request)
        }

        val token = tokenProvider()

        return if (!token.isNullOrBlank()) {
            val newRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()

            chain.proceed(newRequest)
        } else {
            chain.proceed(request)
        }
    }
}
data class RefreshRequest(
    val refresh: String
)