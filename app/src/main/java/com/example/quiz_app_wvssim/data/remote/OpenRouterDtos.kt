package com.example.quiz_app_wvssim.data.remote

import com.google.gson.annotations.SerializedName

data class OpenRouterRequest(
    val model: String = "meta-llama/llama-3.1-8b-instruct:free",
    val messages: List<OpenRouterMessage>,
    @SerializedName("max_tokens") val maxTokens: Int = 250
)

data class OpenRouterMessage(
    val role: String,
    val content: String
)

data class OpenRouterResponse(
    val choices: List<OpenRouterChoice>
)

data class OpenRouterChoice(
    val message: OpenRouterMessage
)
