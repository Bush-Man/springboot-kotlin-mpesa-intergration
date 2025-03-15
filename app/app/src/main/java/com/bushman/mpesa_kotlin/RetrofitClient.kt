package com.bushman.mpesa_kotlin

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
 fun getClient():Retrofit{
     return Retrofit.Builder()
         .baseUrl("http://10.0.2.2:8080")
         .addConverterFactory(GsonConverterFactory.create())
         .build()
 }

    fun getApiService(): ApiService{
        return getClient().create(ApiService::class.java)
    }



 }
