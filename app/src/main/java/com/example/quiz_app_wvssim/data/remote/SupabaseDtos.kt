package com.example.quiz_app_wvssim.data.remote

import com.google.gson.annotations.SerializedName

data class SupabaseQuestionDto(
    val id: String,
    val category: String,
    @SerializedName("prompt") val prompt: String,
    val options: List<String>,
    @SerializedName("correct_index") val correctIndex: Int,
    val explanation: String?,
    @SerializedName("flag_url") val flagUrl: String?,
    @SerializedName("country_description") val countryDescription: String?,
    val hint: String? // Nouveau champ pour l'indice
)

data class SupabaseLeaderboardDto(
    val id: String,
    @SerializedName("user_name") val userName: String,
    val score: Int,
    val category: String,
    @SerializedName("created_at_epoch_ms") val createdAtEpochMs: Long
)

data class SupabaseDuelDto(
    val id: String = "",
    @SerializedName("creator_name") val creatorName: String = "",
    @SerializedName("joiner_name") val joinerName: String? = null,
    val category: String = "",
    val status: String = "waiting",
    @SerializedName("creator_score") val creatorScore: Int = 0,
    @SerializedName("joiner_score") val joinerScore: Int = 0,
    @SerializedName("created_at") val createdAt: String? = null
)

data class SupabaseDuelPatch(
    @SerializedName("joiner_name") val joinerName: String? = null,
    val status: String? = null,
    @SerializedName("creator_score") val creatorScore: Int? = null,
    @SerializedName("joiner_score") val joinerScore: Int? = null
)
