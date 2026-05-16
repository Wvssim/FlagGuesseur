package com.example.quiz_app_wvssim.domain.model

data class LeaderboardEntry(
    val id: String,
    val userName: String,
    val score: Int,
    val category: QuizCategory,
    val createdAtEpochMs: Long
)

