package com.example.quiz_app_wvssim.domain.model

data class QuizQuestion(
    val id: String,
    val category: QuizCategory,
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val flagUrl: String,
    val explanation: String = "",
    val countryDescription: String = "",
    val hint: String = "" // Nouvel indice pour aider l'utilisateur
)
