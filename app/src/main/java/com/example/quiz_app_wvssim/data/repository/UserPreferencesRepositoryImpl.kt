package com.example.quiz_app_wvssim.data.repository

import com.example.quiz_app_wvssim.data.local.UserPreferencesDao
import com.example.quiz_app_wvssim.data.local.UserPreferencesEntity
import com.example.quiz_app_wvssim.domain.model.UserPreferences
import com.example.quiz_app_wvssim.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepositoryImpl(
    private val userPreferencesDao: UserPreferencesDao
) : UserPreferencesRepository {

    override val userPreferencesFlow: Flow<UserPreferences> =
        userPreferencesDao.observePreferences().map { entity ->
            UserPreferences(
                userName = entity?.userName ?: "Explorateur",
                crowns = entity?.crowns ?: 0,
                bestScores = entity?.bestScores ?: emptyList()
            )
        }

    override suspend fun updateCrowns(amount: Int) {
        val current = userPreferencesDao.getPreferences() ?: UserPreferencesEntity()
        userPreferencesDao.upsert(current.copy(crowns = current.crowns + amount))
    }

    override suspend fun saveScore(score: Int) {
        val current = userPreferencesDao.getPreferences() ?: UserPreferencesEntity()
        val newScores = (current.bestScores + score)
            .sortedDescending()
            .take(3)
        userPreferencesDao.upsert(current.copy(bestScores = newScores))
    }

    override suspend fun updateUserName(name: String) {
        val current = userPreferencesDao.getPreferences() ?: UserPreferencesEntity()
        userPreferencesDao.upsert(current.copy(userName = name))
    }
}
