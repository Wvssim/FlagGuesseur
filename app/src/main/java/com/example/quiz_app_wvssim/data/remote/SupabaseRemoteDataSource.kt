package com.example.quiz_app_wvssim.data.remote

import com.example.quiz_app_wvssim.BuildConfig
import com.example.quiz_app_wvssim.domain.model.LeaderboardEntry
import com.example.quiz_app_wvssim.domain.model.QuizCategory
import com.example.quiz_app_wvssim.domain.model.QuizQuestion
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.UUID

class SupabaseRemoteDataSource {
    private val baseUrl = BuildConfig.SUPABASE_URL.trim()
    private val anonKey = BuildConfig.SUPABASE_ANON_KEY.trim()

    private val api: SupabaseApi? = if (baseUrl.isNotEmpty() && anonKey.isNotEmpty()) {
        val authInterceptor = Interceptor { chain ->
            chain.proceed(
                chain.request()
                    .newBuilder()
                    .addHeader("apikey", anonKey)
                    .addHeader("Authorization", "Bearer $anonKey")
                    .build()
            )
        }

        val http = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .build()

        Retrofit.Builder()
            .baseUrl(ensureTrailingSlash(baseUrl))
            .addConverterFactory(GsonConverterFactory.create())
            .client(http)
            .build()
            .create(SupabaseApi::class.java)
    } else {
        null
    }

    suspend fun fetchQuestions(category: QuizCategory): List<QuizQuestion> {
        val service = api ?: return emptyList()
        return service.getQuestions(categoryEq = "eq.${category.name.lowercase()}").map { dto ->
            QuizQuestion(
                id = dto.id,
                category = QuizCategory.fromRaw(dto.category),
                prompt = dto.prompt,
                options = dto.options,
                correctIndex = dto.correctIndex,
                explanation = dto.explanation.orEmpty(),
                flagUrl = dto.flagUrl.orEmpty(),
                countryDescription = dto.countryDescription.orEmpty()
            )
        }
    }

    suspend fun fetchLeaderboard(category: QuizCategory): List<LeaderboardEntry> {
        val service = api ?: return emptyList()
        return service.getLeaderboard(categoryEq = "eq.${category.name.lowercase()}").map { dto ->
            LeaderboardEntry(
                id = dto.id,
                userName = dto.userName,
                score = dto.score,
                category = QuizCategory.fromRaw(dto.category),
                createdAtEpochMs = dto.createdAtEpochMs
            )
        }
    }

    suspend fun sendScore(entry: LeaderboardEntry) {
        val service = api ?: return
        service.insertLeaderboard(
            listOf(
                SupabaseLeaderboardDto(
                    id = if (entry.id.isBlank()) UUID.randomUUID().toString() else entry.id,
                    userName = entry.userName,
                    score = entry.score,
                    category = entry.category.name.lowercase(),
                    createdAtEpochMs = entry.createdAtEpochMs
                )
            )
        )
    }

    suspend fun createDuel(creatorName: String, category: String): SupabaseDuelDto? {
        val service = api ?: return null
        val result = runCatching {
            service.createDuel(SupabaseDuelDto(creatorName = creatorName, category = category))
        }.getOrNull()
        return result?.firstOrNull()
    }

    suspend fun findWaitingDuel(category: String): SupabaseDuelDto? {
        val service = api ?: return null
        return runCatching {
            service.findWaitingDuel(categoryEq = "eq.$category")
        }.getOrNull()?.firstOrNull()
    }

    suspend fun joinDuel(duelId: String, joinerName: String): SupabaseDuelDto? {
        val service = api ?: return null
        return runCatching {
            service.patchDuel(
                idEq = "eq.$duelId",
                patch = SupabaseDuelPatch(joinerName = joinerName, status = "active")
            )
        }.getOrNull()?.firstOrNull()
    }

    suspend fun updateScore(duelId: String, isCreator: Boolean, score: Int): SupabaseDuelDto? {
        val service = api ?: return null
        val patch = if (isCreator)
            SupabaseDuelPatch(creatorScore = score)
        else
            SupabaseDuelPatch(joinerScore = score)
        return runCatching {
            service.patchDuel(idEq = "eq.$duelId", patch = patch)
        }.getOrNull()?.firstOrNull()
    }

    suspend fun finishDuel(duelId: String): SupabaseDuelDto? {
        val service = api ?: return null
        return runCatching {
            service.patchDuel(idEq = "eq.$duelId", patch = SupabaseDuelPatch(status = "finished"))
        }.getOrNull()?.firstOrNull()
    }

    suspend fun pollDuel(duelId: String): SupabaseDuelDto? {
        val service = api ?: return null
        return runCatching {
            service.getDuel(idEq = "eq.$duelId")
        }.getOrNull()?.firstOrNull()
    }

    private fun ensureTrailingSlash(value: String): String = if (value.endsWith('/')) value else "$value/"
}
