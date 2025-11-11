package co.edu.unal.chatbot.data

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAiApi {

    @Headers("Content-Type: application/json")
    @POST("v1beta/openai/chat/completions")   // 👈 NUEVA RUTA
    suspend fun createChatCompletion(
        @Header("Authorization") authHeader: String,
        @Body body: OpenAiChatRequest
    ): OpenAiChatResponse
}

