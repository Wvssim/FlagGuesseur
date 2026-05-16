package com.example.quiz_app_wvssim.ui.navigation

import com.example.quiz_app_wvssim.domain.model.QuizCategory

object QuizRoutes {
    const val HOME = "home"
    const val LIA = "lia"
    const val QUIZ = "quiz/{category}"
    const val RESULT = "result/{category}/{score}/{correct}/{total}/{crowns}"
    const val LEADERBOARD = "leaderboard/{category}"
    const val DUEL = "duel/{category}"

    fun quiz(category: QuizCategory): String = "quiz/${category.name}"

    fun result(category: QuizCategory, score: Int, correct: Int, total: Int, crowns: Int = 0): String =
        "result/${category.name}/$score/$correct/$total/$crowns"

    fun leaderboard(category: QuizCategory): String = "leaderboard/${category.name}"
    fun duel(category: QuizCategory): String = "duel/${category.name}"
}

