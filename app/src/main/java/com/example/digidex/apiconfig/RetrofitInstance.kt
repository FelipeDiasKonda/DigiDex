package com.example.digidex.apiconfig

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://digi-api.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: DigimonApi by lazy {
        retrofit.create(DigimonApi::class.java)
    }
}