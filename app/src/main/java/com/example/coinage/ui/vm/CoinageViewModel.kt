package com.example.coinage.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinage.data.CoinageAsset
import com.example.coinage.data.CoinageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import javax.inject.Inject


@HiltViewModel

class CoinageViewModel @Inject constructor(private val repository: CoinageRepository) :
    ViewModel() {

    private val _assets = MutableStateFlow<List<CoinageAsset>>(emptyList())
    val assets: StateFlow<List<CoinageAsset>> = _assets.asStateFlow() // Expose as StateFlow

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    private var webSocket: WebSocket? = null

    init {
        loadAssets()
        connectWebSocket(CoinageWebSocketListener())
    }

    private fun loadAssets() {
        viewModelScope.launch {
            _assets.value = repository.getAssets()
        }
    }


    private fun connectWebSocket(listener: CoinageWebSocketListener) {
        webSocket = repository.openWebSocket(listener)
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    inner class CoinageWebSocketListener : WebSocketListener() {
        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("WebSocket", "Message: $text")
            try {
                val json = JSONObject(text)
                if (json.has("error")) {
                    val errorMessage = json.getString("error")
                    Log.e("WebSocket", "API Error: $errorMessage")
                } else {
                    val keys = json.keys()
                    while (keys.hasNext()) {
                        val key = keys.next()
                        if (key != "type") {
                            val price = json.getString(key)
                            viewModelScope.launch {
                                val currentAssets = _assets.value.toMutableList()
                                val index = currentAssets.indexOfFirst { it.id == key }
                                if (index != -1) {
                                    currentAssets[index] =
                                        currentAssets[index].copy(priceUsd = price)
                                    _assets.value = currentAssets
                                } else {
                                    Log.w(
                                        "WebSocket",
                                        "Asset $key not found in current list. Fetching full list."
                                    )
                                    _assets.value = repository.getAssets()
                                }
                            }
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e("WebSocket", "JSON parsing error: ${e.message}, Text: $text")
            }

        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.e("WebSocket", "Error: ${t.message}")
            viewModelScope.launch {
                _errorMessage.value = t.message // Set the error message in the ViewModel
            }
        }


        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("WebSocket", "Connection closed: $reason")
            this@CoinageViewModel.webSocket = null

        }


        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("WebSocket", "Connection opened")

            val subscriptionMessage = """
        {
          "type": "subscribe",
          "subscriptions": [
            "global_market",
            "assets"
          ]
        }
    """.trimIndent()

            webSocket.send(subscriptionMessage)
        }

    }

}