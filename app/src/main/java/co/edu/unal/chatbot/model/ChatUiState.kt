package co.edu.unal.chatbot.model

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val currentInput: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
