package co.edu.unal.chatbot.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.unal.chatbot.data.ChatRepository
import co.edu.unal.chatbot.data.NetworkModule
import co.edu.unal.chatbot.model.ChatUiState
import co.edu.unal.chatbot.model.Message
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val repository = ChatRepository(NetworkModule.openAiApi)

    var uiState = androidx.compose.runtime.mutableStateOf(ChatUiState())
        private set

    private var nextId = 0L

    fun onInputChange(text: String) {
        uiState.value = uiState.value.copy(currentInput = text)
    }

    fun onSendClicked() {
        val text = uiState.value.currentInput.trim()
        if (text.isEmpty() || uiState.value.isLoading) return

        val userMessage = Message(
            id = nextId++,
            text = text,
            fromUser = true
        )

        val updatedMessages = uiState.value.messages + userMessage

        uiState.value = uiState.value.copy(
            messages = updatedMessages,
            currentInput = "",
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            try {
                val reply = repository.sendMessage(updatedMessages, text)
                val botMessage = Message(
                    id = nextId++,
                    text = reply,
                    fromUser = false
                )
                uiState.value = uiState.value.copy(
                    messages = uiState.value.messages + botMessage,
                    isLoading = false
                )
            } catch (e: Exception) {
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error: ${e.localizedMessage}"
                )
            }
        }
    }
}
