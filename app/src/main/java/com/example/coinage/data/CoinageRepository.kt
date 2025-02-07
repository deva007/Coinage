package com.example.coinage.data



import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

class CoinageRepository @Inject constructor(
    private val coinCapService: CoinageService,
    private val assetDao: CoinageAssetDao
) {

    suspend fun getAssets(): List<CoinageAsset> = withContext(Dispatchers.IO) {
        try {
            val response = coinCapService.getAssets()
            if (response.isSuccessful) {
                val assets = response.body()?.data
                Log.d("CoinageRepository", "${assets?.size}")
                if (assets != null) {
                    Log.d("CoinageRepository", "${assets.size} == 00")
                    assetDao.insertAll(assets)
                }
                assets
            } else {
                Log.d("CoinageRepository", "00 == 11")
                assetDao.getAllAssets()
            }
        } catch (e: Exception) {
            assetDao.getAllAssets()
        }!!
    }

    private val client = OkHttpClient()

    fun openWebSocket(listener: WebSocketListener): WebSocket {
        val request = Request.Builder().url("wss://ws.coincap.io/prices?assets=ALL").build()
        return client.newWebSocket(request, listener)
    }
}