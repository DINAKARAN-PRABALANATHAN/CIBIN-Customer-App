package com.cibinenterprizes.cibinenterprises.Remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFCMClient {
    private lateinit var instance: Retrofit

    fun getInstance(url: String): Retrofit {
        instance = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return instance
    }
}