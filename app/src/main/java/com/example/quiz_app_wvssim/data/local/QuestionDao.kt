package com.example.quiz_app_wvssim.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quiz_app_wvssim.domain.model.QuizCategory

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions WHERE category = :category ORDER BY id")
    suspend fun getByCategory(category: QuizCategory): List<QuestionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<QuestionEntity>)

    @Query("DELETE FROM questions WHERE category = :category")
    suspend fun deleteByCategory(category: QuizCategory)
}

