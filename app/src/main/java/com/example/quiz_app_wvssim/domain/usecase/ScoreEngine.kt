package com.example.quiz_app_wvssim.domain.usecase

object ScoreEngine {
    private const val BASE_CORRECT_POINTS = 100

    fun pointsForAnswer(isCorrect: Boolean, remainingSeconds: Int): Int {
        if (!isCorrect) return 0
        val speedBonus = (remainingSeconds.coerceAtLeast(0) * 8)
        return BASE_CORRECT_POINTS + speedBonus
    }

    fun accuracy(correctAnswers: Int, totalAnswers: Int): Float {
        if (totalAnswers <= 0) return 0f
        return correctAnswers.toFloat() / totalAnswers.toFloat()
    }
}

