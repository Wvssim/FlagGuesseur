package com.example.quiz_app_wvssim.domain.repository

import com.example.quiz_app_wvssim.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userPreferencesFlow: Flow<UserPreferences>
    suspend fun updateCrowns(amount: Int)
    suspend fun saveScore(score: Int)
    suspend fun updateUserName(name: String)
}
