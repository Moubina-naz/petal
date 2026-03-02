package com.example.petal.ui.Auth

import com.example.petal.domain.AuthResponse
import com.example.petal.domain.LoginReq
import com.example.petal.domain.RegisterReq
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("register/")
    suspend fun register(@Body body: RegisterReq): Response<AuthResponse>

    @POST("login/")
    suspend fun login(@Body body: LoginReq): Response<AuthResponse>
}