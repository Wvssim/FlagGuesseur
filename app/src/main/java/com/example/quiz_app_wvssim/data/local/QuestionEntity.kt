package com.example.quiz_app_wvssim.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.quiz_app_wvssim.domain.model.QuizCategory

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey val id: String,
    val category: QuizCategory,
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val flagUrl: String,
    val countryDescription: String,
    val hint: String, // Nouveau champ pour l'indice
    val updatedAtEpochMs: Long
)
