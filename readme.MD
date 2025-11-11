Here’s a ready-to-paste `README.md` for your project 👇

````markdown
# Chatbot Android App (Gemini + Jetpack Compose)

A simple WhatsApp-style chat application built with **Kotlin**, **Jetpack Compose**, and **Retrofit**, that connects to **Google Gemini** using its **OpenAI-compatible API**.

The app displays a conversation with text bubbles (user on the right, bot on the left) and sends user messages to Gemini, showing the generated replies in real time.

---

## Features

- 📱 **Single-screen chat UI** using Jetpack Compose
- 💬 Bubbles aligned left/right (bot / user)
- 🌐 Network layer with Retrofit + OkHttp + Moshi
- 🤖 Integration with **Gemini OpenAI-compatible endpoint**
- 🔐 API key stored **locally in `local.properties` (never committed to Git)**
- 🧱 Clean separation of layers:
  - `data/` – networking and repository
  - `model/` – UI models (`Message`, `ChatUiState`)
  - `ui/` – Compose screens and ViewModel

---

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose, Material 3
- **Architecture:** ViewModel + state hoisting
- **Networking:** Retrofit, OkHttp, Moshi
- **Coroutines:** `kotlinx-coroutines-android`
- **LLM Provider:** Google Gemini (OpenAI-compatible API)

---

## Project Structure

```text
app/
 └── src/main/java/co/edu/unal/chatbot/
     ├── data/
     │   ├── NetworkModule.kt      // Retrofit + OkHttp + Moshi
     │   ├── OpenAiApi.kt          // Interface for Gemini OpenAI-compatible endpoint
     │   ├── OpenAiModels.kt       // Request/response data classes
     │   └── ChatRepository.kt     // Calls the API and returns the model's reply
     ├── model/
     │   ├── Message.kt            // Single chat message
     │   └── ChatUiState.kt        // UI state for the chat screen
     └── ui/
         ├── ChatViewModel.kt      // Holds state and orchestrates requests
         ├── ChatScreen.kt         // Compose UI (chat bubbles + input bar)
         └── theme/                // Compose theme files (colors, typography, etc.)
````

> Note: Class names like `OpenAiApi`/`OpenAiModels` are kept for compatibility,
> but the backend is **Gemini**, not OpenAI.

---

## Requirements

* **Android Studio** (Koala or newer, with Kotlin 2.x support)
* **JDK 17**
* **minSdk 24** (or as configured in `build.gradle.kts`)
* A **Google Gemini API key** from [Google AI Studio](https://ai.google.dev/gemini-api/docs/api-key)

---

## Getting a Gemini API Key

1. Go to **Google AI Studio**: [https://ai.google.dev/gemini-api/docs/api-key](https://ai.google.dev/gemini-api/docs/api-key)
2. Sign in with your Google account.
3. Create a new API key (for example, `android_chatbot_key`).
4. Copy the key – it looks like `AIzaSy...`. You will only see it **once**, so save it somewhere safe.

---

## Keeping the API Key Private (Very Important) 🔐

The API key **must never** be committed to GitHub.
This project uses the standard Android approach with `local.properties`.

### 1. Add the key to `local.properties`

At the root of the project (same level as `settings.gradle`), open or create a file named `local.properties` and add:

```properties
GEMINI_API_KEY=AIzaSyYourGeminiKeyHere
```

> `local.properties` is already in the default Android `.gitignore`, but double-check it’s ignored before pushing.

### 2. Read the key in `app/build.gradle.kts`

At the top of your `app/build.gradle.kts`:

```kotlin
import java.util.Properties

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}

val geminiApiKey: String = localProperties.getProperty("GEMINI_API_KEY") ?: ""
if (geminiApiKey.isBlank()) {
    error("GEMINI_API_KEY is not defined in local.properties")
}
```

Inside `android { defaultConfig { ... } }`, expose it as a `BuildConfig` field:

```kotlin
defaultConfig {
    applicationId = "co.edu.unal.chatbot"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    buildConfigField(
        "String",
        "OPENAI_API_KEY",       // name kept for compatibility
        "\"$geminiApiKey\""
    )
}
```

### 3. Use it in the repository

In `ChatRepository.kt`:

```kotlin
val response = api.createChatCompletion(
    authHeader = "Bearer ${BuildConfig.OPENAI_API_KEY}",
    body = request
)
```

This way:

* The key lives only in `local.properties` (ignored by Git).
* The app still reads it via `BuildConfig.OPENAI_API_KEY`.

---

## Gemini OpenAI-Compatible Endpoint

The app calls Gemini through the **OpenAI-compatible** endpoint:

* **Base URL:**
  `https://generativelanguage.googleapis.com/`
* **Path:**
  `v1beta/openai/chat/completions`
* **Model:**
  `gemini-2.5-flash` (configurable in `ChatRepository.kt`)

Example Retrofit interface (`OpenAiApi.kt`):

```kotlin
@Headers("Content-Type: application/json")
@POST("v1beta/openai/chat/completions")
suspend fun createChatCompletion(
    @Header("Authorization") authHeader: String,
    @Body body: OpenAiChatRequest
): OpenAiChatResponse
```

The request body uses fields compatible with OpenAI:

```kotlin
data class OpenAiChatRequest(
    val model: String,
    val messages: List<OpenAiMessage>
)

data class OpenAiMessage(
    val role: String,      // "user" or "assistant"
    val content: String
)
```

And the response is parsed from:

```kotlin
data class OpenAiChatResponse(
    val choices: List<Choice>
) {
    data class Choice(
        val index: Int,
        val message: OpenAiMessage
    )
}
```

---

## Running the App

1. **Clone the repository**

   ```bash
   git clone https://github.com/<your-username>/<your-repo>.git
   cd <your-repo>
   ```

2. **Create and fill `local.properties`**

   ```properties
   GEMINI_API_KEY=AIzaSyYourGeminiKeyHere
   ```

3. **Open the project in Android Studio**

   * Wait for Gradle sync to finish.
   * Make sure there are no sync errors.

4. **Run on device/emulator**

   * Select a physical device or an AVD.
   * Click **Run ▶**.

5. Type a message in the input field and tap **Enviar**.
   You should see the bot’s reply rendered as a bubble on the left.

---

## Error Handling

* `HTTP 401` – authentication error (usually invalid or missing API key).
* `HTTP 429` – rate limit / quota exceeded on Gemini.
* Other HTTP errors are shown as `Error HTTP <code>` in the UI.

---

## License

You can add your preferred license here (MIT, Apache 2.0, etc.).

```

Si quieres, después podemos ajustar el README con el nombre real de tu repo y poner capturas de pantalla del chat funcionando.
::contentReference[oaicite:0]{index=0}
```

