package com.example.coinage.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.coinage.R
import com.example.coinage.ui.vm.ProbabilityViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerplexityScreen(viewModel: ProbabilityViewModel = hiltViewModel()) {
    val probabilities by viewModel.probabilities.collectAsState()
    val perplexity by viewModel.perplexity.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.probability)) }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            if (perplexity != null) {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(color = Color.Green.copy(0.17f)),
                    text = "Perplexity: ${String.format("%.2f", perplexity)}",
                    style = MaterialTheme.typography.headlineSmall
                )
            } else if (probabilities.isNotEmpty()) {
                Text(
                    stringResource(R.string.invalid_input_probabilities_must_be_between_0_and_1),
                    color = MaterialTheme.colorScheme.error
                )
            }
            OutlinedTextField(
                value = probabilities,
                onValueChange = { viewModel.updateProbabilities(it) },
                label = { Text(stringResource(R.string.enter_probabilities_comma_separated)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = perplexity == null && probabilities.isNotEmpty(), // Show error if perplexity is null & input is not empty
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))


        }
    }
}