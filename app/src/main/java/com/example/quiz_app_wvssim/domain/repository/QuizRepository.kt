package com.example.quiz_app_wvssim.domain.repository

import com.example.quiz_app_wvssim.domain.model.DuelState
import com.example.quiz_app_wvssim.domain.model.LeaderboardEntry
import com.example.quiz_app_wvssim.domain.model.QuizCategory
import com.example.quiz_app_wvssim.domain.model.QuizQuestion
import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    suspend fun getQuestions(category: QuizCategory): List<QuizQuestion>
    suspend fun getExplorerQuestions(category: QuizCategory): List<QuizQuestion>
    suspend fun refreshLeaderboard(category: QuizCategory)
    fun observeLeaderboard(category: QuizCategory): Flow<List<LeaderboardEntry>>
    suspend fun submitScore(entry: LeaderboardEntry)

    suspend fun createDuel(creatorName: String, category: QuizCategory): String?
    suspend fun findOrCreateDuel(playerName: String, category: QuizCategory): Pair<String, Boolean>?
    suspend fun pollDuel(duelId: String): DuelState?
    suspend fun updateDuelScore(duelId: String, isCreator: Boolean, score: Int)
    suspend fun finishDuel(duelId: String)
}

