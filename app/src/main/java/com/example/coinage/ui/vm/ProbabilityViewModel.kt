package com.example.coinage.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ln
import kotlin.math.pow

@HiltViewModel
class ProbabilityViewModel @Inject constructor() : ViewModel() {

    private val _probabilities = MutableStateFlow("")
    val probabilities: StateFlow<String> = _probabilities.asStateFlow()

    private val _perplexity = MutableStateFlow<Double?>(null)
    val perplexity: StateFlow<Double?> = _perplexity.asStateFlow()

    fun updateProbabilities(input: String) {
        _probabilities.value = input
        calculatePerplexity(input)
    }

    private fun calculatePerplexity(input: String) {
        viewModelScope.launch {
            try {
                val probabilityList = input.split(",").mapNotNull { it.trim().toDoubleOrNull() }

                if (probabilityList.any { it !in 0.0..1.0 }) {
                    _perplexity.value = null
                    return@launch
                }

                if (probabilityList.isEmpty()){
                    _perplexity.value = null
                    return@launch
                }

                val perplexityValue = calculatePerplexityFromList(probabilityList)
                _perplexity.value = perplexityValue

            } catch (e: NumberFormatException) {
                _perplexity.value = null
            }
        }
    }

    private fun calculatePerplexityFromList(probabilities: List<Double>): Double {
        val n = probabilities.size
        var sum = 0.0
        for (p in probabilities) {
            sum += -ln(p) / Math.log(2.0)
        }
        return 2.0.pow(sum / n)
    }
}


