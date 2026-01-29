package com.example.petal.data.remote

import com.example.petal.MemoryRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiProvider {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.29.175:8000/api/") // your real URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val memoryApi: MemoryApi =
        retrofit.create(MemoryApi::class.java)

    val memoryRepository: MemoryRepository =
        MemoryRepository(memoryApi)
}