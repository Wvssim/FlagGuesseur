package com.example.quiz_app_wvssim.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.quiz_app_wvssim.domain.model.QuizCategory

@Entity(
    tableName = "leaderboard",
    indices = [Index(value = ["category", "score"])]
)
data class LeaderboardEntity(
    @PrimaryKey val id: String,
    val userName: String,
    val score: Int,
    val category: QuizCategory,
    val createdAtEpochMs: Long
)

