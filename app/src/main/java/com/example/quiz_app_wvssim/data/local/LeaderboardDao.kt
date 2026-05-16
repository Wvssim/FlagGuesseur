package com.example.quiz_app_wvssim.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quiz_app_wvssim.domain.model.QuizCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface LeaderboardDao {
    @Query("SELECT * FROM leaderboard WHERE category = :category ORDER BY score DESC, createdAtEpochMs ASC LIMIT :limit")
    fun observeTop(category: QuizCategory, limit: Int = 20): Flow<List<LeaderboardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<LeaderboardEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: LeaderboardEntity)

    @Query("DELETE FROM leaderboard WHERE category = :category")
    suspend fun deleteByCategory(category: QuizCategory)
}

