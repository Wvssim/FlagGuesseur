package com.example.quiz_app_wvssim.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiz_app_wvssim.domain.model.DuelState
import com.example.quiz_app_wvssim.domain.model.QuizCategory
import com.example.quiz_app_wvssim.domain.model.QuizQuestion
import com.example.quiz_app_wvssim.domain.repository.QuizRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class DuelPhase { SEARCHING, WAITING_OPPONENT, COUNTDOWN, PLAYING, FINISHED }

data class DuelUiState(
    val phase: DuelPhase = DuelPhase.SEARCHING,
    val duelId: String = "",
    val isCreator: Boolean = false,
    val opponentName: String = "",
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val myScore: Int = 0,
    val opponentScore: Int = 0,
    val lastAnswerCorrect: Boolean? = null,
    val acceptingAnswer: Boolean = true,
    val countdown: Int = 3,
    val remainingSeconds: Int = 12,
    val error: String = ""
)

class DuelViewModel(
    private val repository: QuizRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DuelUiState())
    val state: StateFlow<DuelUiState> = _state.asStateFlow()

    private var pollJob: Job? = null
    private var timerJob: Job? = null

    fun startMatchmaking(playerName: String, category: QuizCategory) {
        viewModelScope.launch {
            _state.update { it.copy(phase = DuelPhase.SEARCHING) }
            val result = repository.findOrCreateDuel(playerName, category)
            if (result == null) {
                _state.update { it.copy(error = "Impossible de rejoindre un duel.") }
                return@launch
            }
            val (duelId, isCreator) = result
            val questions = repository.getQuestions(category).shuffled().take(10)
            _state.update {
                it.copy(
                    duelId = duelId,
                    isCreator = isCreator,
                    questions = questions,
                    phase = if (isCreator) DuelPhase.WAITING_OPPONENT else DuelPhase.COUNTDOWN
                )
            }
            if (isCreator) waitForOpponent(duelId) else startCountdown(duelId)
        }
    }

    private fun waitForOpponent(duelId: String) {
        pollJob = viewModelScope.launch {
            while (_state.value.phase == DuelPhase.WAITING_OPPONENT) {
                delay(2000)
                val duel = repository.pollDuel(duelId) ?: continue
                if (duel.status == "active" && duel.joinerName != null) {
                    _state.update { it.copy(opponentName = duel.joinerName) }
                    startCountdown(duelId)
                    break
                }
            }
        }
    }

    private fun startCountdown(duelId: String) {
        _state.update { it.copy(phase = DuelPhase.COUNTDOWN, countdown = 3) }
        viewModelScope.launch {
            repeat(3) { i ->
                _state.update { it.copy(countdown = 3 - i) }
                delay(1000)
            }
            _state.update { it.copy(phase = DuelPhase.PLAYING) }
            startTimer()
            startPolling(duelId)
        }
    }

    private fun startPolling(duelId: String) {
        pollJob?.cancel()
        pollJob = viewModelScope.launch {
            while (_state.value.phase == DuelPhase.PLAYING) {
                delay(2000)
                val duel = repository.pollDuel(duelId) ?: continue
                val opScore = if (_state.value.isCreator) duel.joinerScore else duel.creatorScore
                val opName = if (_state.value.isCreator) duel.joinerName ?: "" else duel.creatorName
                _state.update { it.copy(opponentScore = opScore, opponentName = opName) }
                if (duel.status == "finished") {
                    _state.update { it.copy(phase = DuelPhase.FINISHED) }
                    break
                }
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_state.value.remainingSeconds > 0 && _state.value.acceptingAnswer) {
                delay(1000)
                _state.update { it.copy(remainingSeconds = it.remainingSeconds - 1) }
            }
            if (_state.value.remainingSeconds == 0 && _state.value.phase == DuelPhase.PLAYING) {
                submitAnswer(-1)
            }
        }
    }

    fun submitAnswer(selectedIndex: Int) {
        val s = _state.value
        if (!s.acceptingAnswer || s.phase != DuelPhase.PLAYING) return
        timerJob?.cancel()

        val question = s.questions.getOrNull(s.currentIndex) ?: return
        val correct = selectedIndex == question.correctIndex
        val points = if (correct) 100 + s.remainingSeconds * 5 else 0
        val newScore = s.myScore + points

        _state.update { it.copy(myScore = newScore, lastAnswerCorrect = correct, acceptingAnswer = false) }

        viewModelScope.launch {
            repository.updateDuelScore(s.duelId, s.isCreator, newScore)
            delay(800)
            val nextIndex = s.currentIndex + 1
            if (nextIndex >= s.questions.size) {
                repository.finishDuel(s.duelId)
                _state.update { it.copy(phase = DuelPhase.FINISHED) }
            } else {
                _state.update {
                    it.copy(
                        currentIndex = nextIndex,
                        lastAnswerCorrect = null,
                        acceptingAnswer = true,
                        remainingSeconds = 12
                    )
                }
                startTimer()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        pollJob?.cancel()
        timerJob?.cancel()
    }
}
