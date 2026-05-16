package com.example.quiz_app_wvssim.domain.model

enum class QuizCategory(val displayName: String) {
    EUROPE("Europe"),
    AFRICA("Afrique"),
    ASIA("Asie"),
    MIDDLE_EAST("Moyen-Orient"),
    AMERICAS("Amériques"),
    OCEANIA("Océanie"),
    WORLD("Monde Entier");

    companion object {
        fun fromRaw(value: String): QuizCategory =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: WORLD
    }
}
