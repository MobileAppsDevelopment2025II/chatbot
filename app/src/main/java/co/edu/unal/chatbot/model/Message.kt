package co.edu.unal.chatbot.model

data class Message(
    val id: Long,
    val text: String,
    val fromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
