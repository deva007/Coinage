package com.example.coinage.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.coinage.R
import com.example.coinage.data.CoinageAsset
import com.example.coinage.ui.vm.CoinageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetsScreen(
    navController: NavController,
    viewModel: CoinageViewModel = hiltViewModel(),
) {
    val errorMessage by viewModel.errorMessage.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val assets = viewModel.assets.collectAsState().value

    SnackbarHost(hostState = snackBarHostState)

    LaunchedEffect(key1 = errorMessage) {
        if (errorMessage != null) {
            snackBarHostState.showSnackbar(
                message = errorMessage!!,
                duration = SnackbarDuration.Short
            )
            viewModel.clearErrorMessage()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.live_prices)) }
            )
        },
        floatingActionButton = { // Add FloatingActionButton
            FloatingActionButton(
                onClick = { navController.navigate("perplexity") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = stringResource(id = R.string.calculate_perplexity)
                )
            }
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
            Text(
                text = formattedPrice,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }
    }
}