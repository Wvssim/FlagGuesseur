package com.example.quiz_app_wvssim.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface SupabaseApi {
    @GET("rest/v1/questions")
    suspend fun getQuestions(
        @Query("select") select: String = "*",
        @Query("category") categoryEq: String,
        @Query("limit") limit: Int = 30
    ): List<SupabaseQuestionDto>

    @GET("rest/v1/leaderboard")
    suspend fun getLeaderboard(
        @Query("select") select: String = "*",
        @Query("category") categoryEq: String,
        @Query("order") order: String = "score.desc,created_at_epoch_ms.asc",
        @Query("limit") limit: Int = 20
    ): List<SupabaseLeaderboardDto>

    @Headers("Prefer: return=representation")
    @POST("rest/v1/leaderboard")
    suspend fun insertLeaderboard(@Body rows: List<SupabaseLeaderboardDto>): List<SupabaseLeaderboardDto>

    @Headers("Prefer: return=representation")
    @POST("rest/v1/duels")
    suspend fun createDuel(@Body duel: SupabaseDuelDto): List<SupabaseDuelDto>

    @GET("rest/v1/duels")
    suspend fun findWaitingDuel(
        @Query("status") statusEq: String = "eq.waiting",
        @Query("category") categoryEq: String,
        @Query("order") order: String = "created_at.asc",
        @Query("limit") limit: Int = 1
    ): List<SupabaseDuelDto>

    @GET("rest/v1/duels")
    suspend fun getDuel(
        @Query("id") idEq: String,
        @Query("select") select: String = "*"
    ): List<SupabaseDuelDto>

    @Headers("Prefer: return=representation")
    @PATCH("rest/v1/duels")
    suspend fun patchDuel(
        @Query("id") idEq: String,
        @Body patch: SupabaseDuelPatch
    ): List<SupabaseDuelDto>
}

