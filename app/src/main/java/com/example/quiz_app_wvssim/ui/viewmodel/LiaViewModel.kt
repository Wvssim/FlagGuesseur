package com.example.quiz_app_wvssim.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiz_app_wvssim.data.remote.AiGeneratedQuestion
import com.example.quiz_app_wvssim.data.remote.OpenRouterDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LiaUiState(
    val selectedCountry: String = "",
    val selectedContinent: String = "",
    val explanation: String = "",
    val isLoadingExplanation: Boolean = false,
    val qcm: AiGeneratedQuestion? = null,
    val isLoadingQcm: Boolean = false,
    val selectedAnswer: Int? = null
)

data class CountryInfo(val name: String, val capital: String)

class LiaViewModel(private val openRouter: OpenRouterDataSource) : ViewModel() {

    private val _uiState = MutableStateFlow(LiaUiState())
    val uiState: StateFlow<LiaUiState> = _uiState.asStateFlow()

    private val continentCountries = mapOf(
        "Afrique" to listOf("Égypte", "Nigeria", "Afrique du Sud", "Kenya", "Maroc", "Algérie", "Éthiopie", "Ghana"),
        "Asie" to listOf("Japon", "Chine", "Inde", "Thaïlande", "Vietnam", "Corée du Sud", "Singapour", "Malaisie"),
        "Europe" to listOf("France", "Italie", "Espagne", "Allemagne", "Royaume-Uni", "Suisse", "Belgique", "Pays-Bas"),
        "Amérique du Nord" to listOf("États-Unis", "Canada", "Mexique", "Cuba", "République Dominicaine"),
        "Amérique du Sud" to listOf("Brésil", "Argentine", "Chili", "Pérou", "Colombie", "Équateur"),
        "Océanie" to listOf("Australie", "Nouvelle-Zélande", "Fidji", "Samoa")
    )

    fun getContinents(): List<String> = continentCountries.keys.toList()

    fun getCountriesByContinent(continent: String): List<String> {
        return continentCountries[continent] ?: emptyList()
    }

    fun selectContinent(continent: String) {
        _uiState.update {
            it.copy(
                selectedContinent = continent,
                selectedCountry = "",
                explanation = "",
                qcm = null,
                selectedAnswer = null
            )
        }
    }

    fun selectCountry(country: String) {
        _uiState.update {
            it.copy(
                selectedCountry = country,
                explanation = "",
                qcm = null,
                selectedAnswer = null
            )
        }
    }

    fun requestExplanation() {
        val country = _uiState.value.selectedCountry.trim()
        if (country.isEmpty()) return
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoadingExplanation = true,
                    explanation = "",
                    qcm = null,
                    selectedAnswer = null
                )
            }
            val result = openRouter.askLia("Donne-moi 3 infos fascinantes et rapides sur $country (capitale, culture, géographie, histoire)")
            _uiState.update { it.copy(isLoadingExplanation = false, explanation = result) }
        }
    }

    fun generateQcm() {
        val country = _uiState.value.selectedCountry.trim()
        if (country.isEmpty()) return
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoadingQcm = true,
                    qcm = null,
                    selectedAnswer = null,
                    explanation = ""
                )
            }
            var result = openRouter.generateQcmOnTopic("$country : capitale, géographie, culture ou histoire")

            // Fallback: utiliser QCM de démo si l'API échoue
            if (result == null) {
                result = openRouter.getDefaultQcms()[country]?.randomOrNull()
            }

            _uiState.update { it.copy(isLoadingQcm = false, qcm = result) }
        }
    }

    fun selectAnswer(index: Int) {
        if (_uiState.value.selectedAnswer == null) {
            _uiState.update { it.copy(selectedAnswer = index) }
        }
    }

    fun reset() {
        _uiState.update { LiaUiState() }
    }
}
