package com.example.quiz_app_wvssim.di

import android.content.Context
import com.example.quiz_app_wvssim.data.local.QuizDatabase
import com.example.quiz_app_wvssim.data.remote.OpenRouterDataSource
import com.example.quiz_app_wvssim.data.remote.SupabaseRemoteDataSource
import com.example.quiz_app_wvssim.data.repository.QuizRepositoryImpl
import com.example.quiz_app_wvssim.data.repository.UserPreferencesRepositoryImpl
import com.example.quiz_app_wvssim.domain.repository.QuizRepository
import com.example.quiz_app_wvssim.domain.repository.UserPreferencesRepository

class AppContainer(context: Context) {
    private val appContext = context.applicationContext
    private val database by lazy { QuizDatabase.create(appContext) }
    private val remote by lazy { SupabaseRemoteDataSource() }
    val openRouter by lazy { OpenRouterDataSource() }

    val quizRepository: QuizRepository by lazy {
        QuizRepositoryImpl(
            questionDao = database.questionDao(),
            leaderboardDao = database.leaderboardDao(),
            remote = remote
        )
    }

    val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepositoryImpl(database.userPreferencesDao())
    }
}
