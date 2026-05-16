package com.example.quiz_app_wvssim.domain.model

data class DuelState(
    val id: String,
    val creatorName: String,
    val joinerName: String?,
    val category: QuizCategory,
    val status: String,
    val creatorScore: Int,
    val joinerScore: Int
)
