package com.example.quiz_app_wvssim.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quiz_app_wvssim.data.remote.OpenRouterDataSource
import com.example.quiz_app_wvssim.domain.repository.QuizRepository
import com.example.quiz_app_wvssim.domain.repository.UserPreferencesRepository

class AppViewModelFactory(
    private val quizRepository: QuizRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val openRouter: OpenRouterDataSource? = null
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(QuizViewModel::class.java) ->
                QuizViewModel(quizRepository, userPreferencesRepository, openRouter) as T
            modelClass.isAssignableFrom(LeaderboardViewModel::class.java) ->
                LeaderboardViewModel(quizRepository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                HomeViewModel(userPreferencesRepository) as T
            modelClass.isAssignableFrom(DuelViewModel::class.java) ->
                DuelViewModel(quizRepository) as T
            modelClass.isAssignableFrom(LiaViewModel::class.java) ->
                LiaViewModel(openRouter ?: throw IllegalStateException("OpenRouter required for LiaViewModel")) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
