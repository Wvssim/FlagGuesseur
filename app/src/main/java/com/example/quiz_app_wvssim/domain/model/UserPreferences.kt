package com.example.quiz_app_wvssim.domain.model

data class UserPreferences(
    val userName: String = "Explorateur",
    val crowns: Int = 0,
    val bestScores: List<Int> = emptyList() // Garde les 3 meilleurs scores
)
