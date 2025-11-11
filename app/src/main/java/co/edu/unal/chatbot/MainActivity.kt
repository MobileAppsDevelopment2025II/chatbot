package co.edu.unal.chatbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import co.edu.unal.chatbot.ui.ChatScreen
import co.edu.unal.chatbot.ui.ChatViewModel
import co.edu.unal.chatbot.ui.theme.ChatbotTheme

class MainActivity : ComponentActivity() {

    private val vm: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatbotTheme {
                ChatScreen(viewModel = vm)
            }
        }
    }
}
