package com.example.quiz_app_wvssim.ui.viewmodel

enum class GameMode(val displayName: String, val description: String) {
    QUIZ("Classique", "Le mode standard avec 3 vies."),
    SURVIVAL("Survie", "Une seule vie. Le chrono s'accélère !"),
    TIME_ATTACK("Contre-la-montre", "60s au départ. +3s par bonne réponse."),
    CAPITALS("Capitales", "Trouve la capitale du pays affiché."),
    ZEN("Zen", "Pas de limite de temps ni de vies."),
    VOCAL("Vocal", "Dis le nom du pays à voix haute !"),
    DUEL("Duel", "Affronte un joueur en temps réel !"),
    EXPLORER("Explorateur", "Drapeau + questions sur la capitale, la langue, la monnaie…"),
    VS_AI("Vs IA", "Affronte ARIA, l'intelligence artificielle !")
}
