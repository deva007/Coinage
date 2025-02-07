package com.example.coinage

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.coinage.data.CoinageAsset
import com.example.coinage.ui.theme.CoinageTheme
import com.example.coinage.ui.theme.vm.CoinageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: CoinageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoinageTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AssetsScreen(viewModel)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetsScreen(viewModel: CoinageViewModel) {

    val assets = viewModel.assets.collectAsState().value

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Live Rates") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            if (assets.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .wrapContentHeight()
                )
            } else {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(assets) { asset ->
                        AssetItem(asset)
                    }
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun AssetItem(asset: CoinageAsset) {
    Card(
        colors = CardColors(Color.White, Color.Black, Color.White, Color.Gray),
        modifier = Modifier
            .background(color = Color.White.copy(alpha = 0.11f))
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Green.copy(alpha = 0.17f))
                .padding(16.dp)
        ) {
            Text(text = asset.name, style = MaterialTheme.typography.headlineSmall)
            Text(text = asset.symbol, style = MaterialTheme.typography.bodyMedium)
            val formattedPrice = String.format("%.2f", asset.priceUsd.toDoubleOrNull() ?: 0.0)
            Text(text = formattedPrice, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
        }
    }
}
