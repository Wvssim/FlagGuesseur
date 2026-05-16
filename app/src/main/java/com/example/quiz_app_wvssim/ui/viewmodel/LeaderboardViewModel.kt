package com.example.quiz_app_wvssim.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiz_app_wvssim.domain.model.LeaderboardEntry
import com.example.quiz_app_wvssim.domain.model.QuizCategory
import com.example.quiz_app_wvssim.domain.repository.QuizRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class LeaderboardUiState(
    val loading: Boolean = true,
    val category: QuizCategory? = null,
    val entries: List<LeaderboardEntry> = emptyList()
)

class LeaderboardViewModel(
    private val repository: QuizRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LeaderboardUiState())
    val uiState: StateFlow<LeaderboardUiState> = _uiState.asStateFlow()

    private var observeJob: Job? = null

    fun bind(category: QuizCategory) {
        if (_uiState.value.category == category && observeJob != null) return

        observeJob?.cancel()
        _uiState.update { it.copy(loading = true, category = category) }

        observeJob = viewModelScope.launch {
            launch { repository.refreshLeaderboard(category) }
            repository.observeLeaderboard(category).collect { items ->
                _uiState.value = LeaderboardUiState(
                    loading = false,
                    category = category,
                    entries = items
                )
            }
        }
    }

    fun submitScoreIfNeeded(category: QuizCategory, score: Int, userName: String) {
        viewModelScope.launch {
            repository.submitScore(
                LeaderboardEntry(
                    id = UUID.randomUUID().toString(),
                    userName = userName,
                    score = score,
                    category = category,
                    createdAtEpochMs = System.currentTimeMillis()
                )
            )
            repository.refreshLeaderboard(category)
        }
    }
}

