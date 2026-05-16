package com.example.quiz_app_wvssim.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface OpenRouterApi {
    @POST("chat/completions")
    suspend fun chat(@Body request: OpenRouterRequest): OpenRouterResponse
}
