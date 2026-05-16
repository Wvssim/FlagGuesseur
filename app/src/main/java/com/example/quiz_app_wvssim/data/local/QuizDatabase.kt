package com.example.quiz_app_wvssim.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [QuestionEntity::class, LeaderboardEntity::class, UserPreferencesEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(QuizTypeConverters::class)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun leaderboardDao(): LeaderboardDao
    abstract fun userPreferencesDao(): UserPreferencesDao

    companion object {
        fun create(context: Context): QuizDatabase =
            Room.databaseBuilder(context, QuizDatabase::class.java, "quiz_app.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}
