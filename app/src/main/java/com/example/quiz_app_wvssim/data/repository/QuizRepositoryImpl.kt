package com.example.quiz_app_wvssim.data.repository

import com.example.quiz_app_wvssim.data.local.LeaderboardDao
import com.example.quiz_app_wvssim.data.local.LeaderboardEntity
import com.example.quiz_app_wvssim.data.local.QuestionDao
import com.example.quiz_app_wvssim.data.local.QuestionEntity
import com.example.quiz_app_wvssim.data.remote.SupabaseRemoteDataSource
import com.example.quiz_app_wvssim.domain.model.DuelState
import com.example.quiz_app_wvssim.domain.model.LeaderboardEntry
import com.example.quiz_app_wvssim.domain.model.QuizCategory
import com.example.quiz_app_wvssim.domain.model.QuizQuestion
import com.example.quiz_app_wvssim.domain.repository.QuizRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QuizRepositoryImpl(
    private val questionDao: QuestionDao,
    private val leaderboardDao: LeaderboardDao,
    private val remote: SupabaseRemoteDataSource
) : QuizRepository {

    override suspend fun getQuestions(category: QuizCategory): List<QuizQuestion> {
        val cached = questionDao.getByCategory(category).map { it.toDomain() }
        val remoteQuestions = runCatching { remote.fetchQuestions(category) }.getOrDefault(emptyList())

        return when {
            remoteQuestions.isNotEmpty() -> {
                questionDao.deleteByCategory(category)
                questionDao.upsertAll(remoteQuestions.map { it.toEntity() })
                remoteQuestions
            }
            cached.isNotEmpty() -> cached
            else -> {
                val seeded = SeedData.questions(category)
                questionDao.upsertAll(seeded.map { it.toEntity() })
                seeded
            }
        }
    }

    override suspend fun getExplorerQuestions(category: QuizCategory): List<QuizQuestion> =
        SeedData.explorerQuestions(category)

    override suspend fun refreshLeaderboard(category: QuizCategory) {
        val remoteEntries = runCatching { remote.fetchLeaderboard(category) }.getOrDefault(emptyList())
        if (remoteEntries.isNotEmpty()) {
            leaderboardDao.deleteByCategory(category)
            leaderboardDao.upsertAll(remoteEntries.map { it.toEntity() })
        }
    }

    override fun observeLeaderboard(category: QuizCategory): Flow<List<LeaderboardEntry>> =
        leaderboardDao.observeTop(category).map { rows -> rows.map { it.toDomain() } }

    override suspend fun submitScore(entry: LeaderboardEntry) {
        leaderboardDao.upsert(entry.toEntity())
        runCatching { remote.sendScore(entry) }
    }

    override suspend fun createDuel(creatorName: String, category: QuizCategory): String? =
        remote.createDuel(creatorName, category.name.lowercase())?.id

    override suspend fun findOrCreateDuel(playerName: String, category: QuizCategory): Pair<String, Boolean>? {
        val existing = remote.findWaitingDuel(category.name.lowercase())
        return if (existing != null) {
            remote.joinDuel(existing.id, playerName)
            Pair(existing.id, false)
        } else {
            val newDuel = remote.createDuel(playerName, category.name.lowercase()) ?: return null
            Pair(newDuel.id, true)
        }
    }

    override suspend fun pollDuel(duelId: String): DuelState? {
        val dto = remote.pollDuel(duelId) ?: return null
        return DuelState(
            id = dto.id,
            creatorName = dto.creatorName,
            joinerName = dto.joinerName,
            category = QuizCategory.fromRaw(dto.category),
            status = dto.status,
            creatorScore = dto.creatorScore,
            joinerScore = dto.joinerScore
        )
    }

    override suspend fun updateDuelScore(duelId: String, isCreator: Boolean, score: Int) {
        runCatching { remote.updateScore(duelId, isCreator, score) }
    }

    override suspend fun finishDuel(duelId: String) {
        runCatching { remote.finishDuel(duelId) }
    }
}

private fun QuestionEntity.toDomain() = QuizQuestion(
    id = id,
    category = category,
    prompt = prompt,
    options = options,
    correctIndex = correctIndex,
    explanation = explanation,
    flagUrl = flagUrl,
    countryDescription = countryDescription,
    hint = hint
)

private fun QuizQuestion.toEntity() = QuestionEntity(
    id = id,
    category = category,
    prompt = prompt,
    options = options,
    correctIndex = correctIndex,
    explanation = explanation,
    flagUrl = flagUrl,
    countryDescription = countryDescription,
    hint = hint,
    updatedAtEpochMs = System.currentTimeMillis()
)

private fun LeaderboardEntry.toEntity() = LeaderboardEntity(
    id = id,
    userName = userName,
    score = score,
    category = category,
    createdAtEpochMs = createdAtEpochMs
)

private fun LeaderboardEntity.toDomain() = LeaderboardEntry(
    id = id,
    userName = userName,
    score = score,
    category = category,
    createdAtEpochMs = createdAtEpochMs
)
