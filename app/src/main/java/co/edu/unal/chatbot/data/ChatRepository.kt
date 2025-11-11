package co.edu.unal.chatbot.data

import co.edu.unal.chatbot.BuildConfig
import co.edu.unal.chatbot.model.Message

class ChatRepository(
    private val api: OpenAiApi
) {

    suspend fun sendMessage(messages: List<Message>, newUserMessage: String): String {
        // Convertimos el historial a formato OpenAI
        val historyAsOpenAi = buildList {
            messages.forEach { msg ->
                add(
                    OpenAiMessage(
                        role = if (msg.fromUser) "user" else "assistant",
                        content = msg.text
                    )
                )
            }
            // Último mensaje del usuario
            add(OpenAiMessage(role = "user", content = newUserMessage))
        }

        val request = OpenAiChatRequest(
            model = "gemini-2.5-flash",  // modelo recomendado
            messages = historyAsOpenAi
        )


        val response = api.createChatCompletion(
            authHeader = "Bearer ${BuildConfig.OPENAI_API_KEY}",
            body = request
        )

        val firstChoice = response.choices.firstOrNull()
            ?: error("No se recibió respuesta del modelo")

        return firstChoice.message.content
    }
}
