package co.edu.unal.chatbot.data

data class OpenAiChatRequest(
    val model: String,
    val messages: List<OpenAiMessage>
)

data class OpenAiMessage(
    val role: String,      // "user" o "assistant"
    val content: String
)

data class OpenAiChatResponse(
    val choices: List<Choice>
) {
    data class Choice(
        val index: Int,
        val message: OpenAiMessage
    )
}
