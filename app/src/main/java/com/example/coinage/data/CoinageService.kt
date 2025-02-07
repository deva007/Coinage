package com.example.coinage.data

import retrofit2.Response
import retrofit2.http.GET

interface CoinageService {

    @GET("assets")
    suspend fun getAssets(): Response<AssetsResponse>

}
