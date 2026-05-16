package com.example.quiz_app_wvssim.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiz_app_wvssim.data.remote.OpenRouterDataSource
import com.example.quiz_app_wvssim.domain.model.QuizCategory
import com.example.quiz_app_wvssim.domain.model.QuizQuestion
import com.example.quiz_app_wvssim.domain.repository.QuizRepository
import com.example.quiz_app_wvssim.domain.repository.UserPreferencesRepository
import com.example.quiz_app_wvssim.domain.usecase.ScoreEngine
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class QuizUiState(
    val loading: Boolean = true,
    val category: QuizCategory? = null,
    val currentQuestion: QuizQuestion? = null,
    val questionIndex: Int = 0,
    val totalQuestions: Int = 0,
    val score: Int = 0,
    val correctAnswers: Int = 0,
    val remainingSeconds: Int = 15,
    val totalTimeRemaining: Int = 60,
    val acceptingAnswer: Boolean = true,
    val finished: Boolean = false,
    val lastAnswerCorrect: Boolean? = null,
    val lives: Int = 3,
    val gameMode: GameMode = GameMode.QUIZ,
    val userCrowns: Int = 0,
    val hintRevealed: Boolean = false,
    val canAffordHint: Boolean = false,
    val canAffordRevive: Boolean = false,
    val showReviveOption: Boolean = false,
    val currentReviveCost: Int = 200,
    val currentTimerMax: Int = 15,
    val earnedCrowns: Int = 0,
    // VS AI
    val aiScore: Int = 0,
    val aiCorrectAnswers: Int = 0,
    val aiIsThinking: Boolean = false,
    val aiAnsweredCorrect: Boolean? = null,
    // AI Hint
    val aiExplanation: String = "",
    val aiExplanationLoading: Boolean = false
)

class QuizViewModel(
    private val quizRepository: QuizRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val openRouter: OpenRouterDataSource? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private var questions: List<QuizQuestion> = emptyList()
    private var timerJob: Job? = null
    
    private val HINT_COST = 50

    init {
        userPreferencesRepository.userPreferencesFlow
            .onEach { prefs ->
                val crowns = prefs.crowns
                val (timerMax, reviveCost) = when {
                    crowns < 500 -> 15 to 200
                    crowns < 5000 -> 10 to 500
                    else -> 7 to 1000
                }
                
                _uiState.update { it.copy(
                    userCrowns = crowns,
                    canAffordHint = crowns >= HINT_COST,
                    canAffordRevive = crowns >= reviveCost,
                    currentReviveCost = reviveCost,
                    currentTimerMax = timerMax
                ) }
            }.launchIn(viewModelScope)
    }

    fun start(category: QuizCategory, mode: GameMode = GameMode.QUIZ) {
        viewModelScope.launch {
            val initialLives = when(mode) {
                GameMode.SURVIVAL -> 1
                GameMode.ZEN -> 999
                else -> 3
            }

            _uiState.update { it.copy(
                loading = true,
                category = category,
                gameMode = mode,
                lives = initialLives,
                score = 0,
                questionIndex = 0,
                showReviveOption = false,
                totalTimeRemaining = 60,
                correctAnswers = 0,
                aiScore = 0,
                aiCorrectAnswers = 0,
                aiIsThinking = false,
                aiAnsweredCorrect = null,
                aiExplanation = "",
                aiExplanationLoading = false
            ) }
            
            questions = if (mode == GameMode.EXPLORER)
                quizRepository.getExplorerQuestions(category).shuffled()
            else
                quizRepository.getQuestions(category).shuffled()
            val first = questions.firstOrNull()
            _uiState.update {
                it.copy(
                    loading = false,
                    currentQuestion = first,
                    totalQuestions = if (mode == GameMode.TIME_ATTACK || mode == GameMode.SURVIVAL) 999 else questions.size,
                    remainingSeconds = getInitialTimeForMode(mode, 0, it.currentTimerMax),
                    acceptingAnswer = true,
                    finished = first == null,
                    hintRevealed = false
                )
            }
            if (first != null) startTimer()
        }
    }

    private fun getInitialTimeForMode(mode: GameMode, index: Int, baseTime: Int): Int {
        return when(mode) {
            GameMode.SURVIVAL -> (baseTime - (index / 3)).coerceAtLeast(4)
            GameMode.TIME_ATTACK -> 999 
            else -> baseTime
        }
    }

    fun useHint() {
        val state = _uiState.value
        if (state.canAffordHint && !state.hintRevealed && state.acceptingAnswer) {
            viewModelScope.launch {
                userPreferencesRepository.updateCrowns(-HINT_COST)
                _uiState.update { it.copy(hintRevealed = true) }
            }
        }
    }

    fun revive() {
        val state = _uiState.value
        if (state.canAffordRevive && state.showReviveOption) {
            viewModelScope.launch {
                userPreferencesRepository.updateCrowns(-state.currentReviveCost)
                _uiState.update { it.copy(lives = 1, showReviveOption = false, acceptingAnswer = true) }
                goToNextQuestion()
            }
        }
    }

    fun submitAnswer(selectedIndex: Int) {
        val state = _uiState.value
        val question = state.currentQuestion ?: return
        if (!state.acceptingAnswer || state.finished) return

        if (state.gameMode != GameMode.TIME_ATTACK) timerJob?.cancel()
        
        val correct = selectedIndex == question.correctIndex
        
        var earnedPoints = if (correct && state.gameMode != GameMode.ZEN) {
            val base = if (state.gameMode == GameMode.CAPITALS) 150 else 100
            val speedBonus = if (state.gameMode == GameMode.TIME_ATTACK) 0 else (state.remainingSeconds * 5)
            base + speedBonus
        } else 0
        
        if (state.gameMode == GameMode.SURVIVAL && correct) earnedPoints *= 2

        val newLives = if (correct || state.gameMode == GameMode.ZEN || state.gameMode == GameMode.TIME_ATTACK) state.lives else state.lives - 1

        if (state.gameMode == GameMode.TIME_ATTACK) {
            _uiState.update { it.copy(
                totalTimeRemaining = (it.totalTimeRemaining + if (correct) 3 else -8).coerceAtLeast(0)
            ) }
        }

        _uiState.update {
            it.copy(
                score = it.score + earnedPoints,
                correctAnswers = it.correctAnswers + if (correct) 1 else 0,
                acceptingAnswer = false,
                lastAnswerCorrect = correct,
                lives = newLives
            )
        }

        if (state.gameMode == GameMode.VS_AI) {
            simulateAiAnswer(question.correctIndex)
        } else {
            viewModelScope.launch {
                delay(800)
                val isGameOver = (newLives <= 0) ||
                    (state.gameMode == GameMode.TIME_ATTACK && _uiState.value.totalTimeRemaining <= 0) ||
                    (state.gameMode != GameMode.SURVIVAL && state.gameMode != GameMode.TIME_ATTACK && state.questionIndex + 1 >= questions.size)

                if (isGameOver) {
                    if (state.canAffordRevive && state.gameMode == GameMode.QUIZ) {
                        _uiState.update { it.copy(showReviveOption = true) }
                    } else {
                        finishGame()
                    }
                } else {
                    goToNextQuestion()
                }
            }
        }
    }

    private suspend fun finishGame() {
        timerJob?.cancel()
        val state = _uiState.value
        var earned = 0
        if (state.gameMode != GameMode.ZEN) {
            val divisor = when(state.gameMode) {
                GameMode.TIME_ATTACK -> 50
                GameMode.SURVIVAL -> 15
                else -> 10
            }
            earned = (state.score / divisor).coerceAtMost(500)
            userPreferencesRepository.updateCrowns(earned)
            userPreferencesRepository.saveScore(state.score)
        }
        _uiState.update { it.copy(finished = true, currentQuestion = null, showReviveOption = false, earnedCrowns = earned) }
    }

    fun requestAiExplanation() {
        val state = _uiState.value
        val question = state.currentQuestion ?: return
        if (state.aiExplanationLoading || state.aiExplanation.isNotEmpty()) return
        viewModelScope.launch {
            _uiState.update { it.copy(aiExplanationLoading = true) }
            val text = openRouter?.getExplanation(
                question = question.prompt,
                correctAnswer = question.options.getOrNull(question.correctIndex) ?: ""
            ) ?: "OpenRouter non configuré."
            _uiState.update { it.copy(aiExplanation = text, aiExplanationLoading = false) }
        }
    }

    private fun simulateAiAnswer(correctIndex: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(aiIsThinking = true, aiAnsweredCorrect = null) }
            delay((2000L..4500L).random())
            val aiCorrect = Math.random() < 0.80
            val points = if (aiCorrect) 100 else 0
            _uiState.update { it.copy(
                aiIsThinking = false,
                aiAnsweredCorrect = aiCorrect,
                aiScore = it.aiScore + points,
                aiCorrectAnswers = it.aiCorrectAnswers + if (aiCorrect) 1 else 0
            ) }
            delay(1500L)
            val isGameOver = (_uiState.value.lives <= 0) ||
                (_uiState.value.gameMode != GameMode.SURVIVAL && _uiState.value.gameMode != GameMode.TIME_ATTACK && _uiState.value.questionIndex + 1 >= questions.size)
            if (isGameOver) finishGame() else goToNextQuestion()
        }
    }

    private fun goToNextQuestion() {
        val nextIndex = (_uiState.value.questionIndex + 1) % questions.size
        _uiState.update {
            it.copy(
                questionIndex = _uiState.value.questionIndex + 1,
                currentQuestion = questions[nextIndex],
                remainingSeconds = getInitialTimeForMode(it.gameMode, nextIndex, it.currentTimerMax),
                acceptingAnswer = true,
                lastAnswerCorrect = null,
                hintRevealed = false,
                aiAnsweredCorrect = null,
                aiExplanation = "",
                aiExplanationLoading = false
            )
        }
        if (_uiState.value.gameMode != GameMode.TIME_ATTACK) startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            if (_uiState.value.gameMode == GameMode.TIME_ATTACK) {
                while (_uiState.value.totalTimeRemaining > 0 && !_uiState.value.finished) {
                    delay(1000)
                    _uiState.update { it.copy(totalTimeRemaining = it.totalTimeRemaining - 1) }
                    if (_uiState.value.totalTimeRemaining <= 0) finishGame()
                }
            } else {
                while (_uiState.value.remainingSeconds > 0 && _uiState.value.acceptingAnswer) {
                    delay(1000)
                    _uiState.update { state ->
                        state.copy(remainingSeconds = (state.remainingSeconds - 1).coerceAtLeast(0))
                    }
                }
                if (_uiState.value.remainingSeconds == 0 && _uiState.value.acceptingAnswer) {
                    submitAnswer(selectedIndex = -1)
                }
            }
        }
    }
}
