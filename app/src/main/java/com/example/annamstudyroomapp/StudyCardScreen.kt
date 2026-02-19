//StudyCardScreen.kt
package com.example.annamstudyroomapp

import android.content.Context
import android.util.Base64
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/* ---------- Utils ---------- */

fun saveAudioToInternalStorage(
    context: Context,
    audioData: ByteArray,
    filename: String
) {
    val file = File(context.filesDir, filename)
    FileOutputStream(file).use { it.write(audioData) }
}

/* ---------- Screen ---------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyCardScreen(
    navigateBack: () -> Unit,
    getRandomLesson: suspend (Int) -> List<FlashCard>,
    updateFlashCardByPair: suspend (
        String, String, String, String, String
    ) -> Unit,
    changeMessage: (String) -> Unit = {},
    networkService: NetworkService
) {
    val context = LocalContext.current
    val appContext = context.applicationContext
    val scope = rememberCoroutineScope()

    var lessonCards by remember { mutableStateOf<List<FlashCard>>(emptyList()) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var showEnglish by remember { mutableStateOf(true) }

    var audioKey by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }

    /* -------- ExoPlayer -------- */
    val player = remember {
        ExoPlayer.Builder(context).build()
    }

    DisposableEffect(Unit) {
        onDispose { player.release() }
    }

    /* ---------- Load lesson ---------- */
    LaunchedEffect(Unit) {
        try {
            lessonCards = getRandomLesson(3)
            if (lessonCards.isEmpty()) {
                changeMessage("No flashcards found")
            } else {
                currentIndex = 0
                showEnglish = true
            }
        } catch (e: Exception) {
            changeMessage("Error loading lesson")
        }
    }

    /* ---------- Load audio key from DB ---------- */
    LaunchedEffect(currentIndex) {
        if (lessonCards.isNotEmpty()) {
            audioKey = lessonCards[currentIndex].audioCard.orEmpty()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Study Card", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (lessonCards.isNotEmpty()) {
                val card = lessonCards[currentIndex]

                /* ---------- CARD TEXT ---------- */
                OutlinedButton(
                    onClick = { showEnglish = !showEnglish },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (showEnglish)
                            card.englishCard.orEmpty()
                        else
                            card.vietnameseCard.orEmpty(),
                        fontSize = 18.sp
                    )
                }

                if (!showEnglish) {

                    /* ---------- PLAY AUDIO ---------- */
                    Button(
                        onClick = {
                            if (audioKey.isBlank()) {
                                changeMessage("No audio")
                                return@Button
                            }

                            val file = File(context.filesDir, "$audioKey.mp3")
                            if (!file.exists()) {
                                changeMessage("Audio not generated")
                                return@Button
                            }

                            player.setMediaItem(
                                MediaItem.fromUri(file.toUri())
                            )
                            player.prepare()
                            player.play()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Play")
                    }

                    /* ---------- GENERATE AUDIO ---------- */
                    Button(
                        enabled = !isGenerating,
                        onClick = {
                            scope.launch {
                                val prefs =
                                    appContext.dataStore.data.first()
                                val email = prefs[EMAIL].orEmpty()
                                val token = prefs[TOKEN].orEmpty()

                                if (email.isBlank() || token.isBlank()) {
                                    changeMessage("User not logged in")
                                    return@launch
                                }

                                isGenerating = true
                                val key =
                                    generateAudioKey(card.vietnameseCard.orEmpty())
                                audioKey = key

                                try {
                                    val result = withContext(Dispatchers.IO) {
                                        networkService.generateAudio(
                                            email = AudioCredential(
                                                word = card.vietnameseCard.orEmpty(),
                                                email = email,
                                                token = token
                                            )
                                        )
                                    }

                                    if (result.code != 200) {
                                        changeMessage("Generate failed")
                                        return@launch
                                    }

                                    val audioBytes =
                                        Base64.decode(
                                            result.message,
                                            Base64.DEFAULT
                                        )

                                    saveAudioToInternalStorage(
                                        context,
                                        audioBytes,
                                        "$key.mp3"
                                    )

                                    changeMessage("Audio generated")
                                } catch (e: Exception) {
                                    changeMessage("Error: ${e.message}")
                                } finally {
                                    isGenerating = false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isGenerating) "Generating..." else "Generate Audio")
                    }

                    /* ---------- SAVE AUDIO KEY ---------- */
                    Button(
                        onClick = {
                            scope.launch {
                                updateFlashCardByPair(
                                    card.englishCard.orEmpty(),
                                    card.vietnameseCard.orEmpty(),
                                    card.englishCard.orEmpty(),
                                    card.vietnameseCard.orEmpty(),
                                    audioKey
                                )
                                changeMessage("Audio saved")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save")
                    }

                    /* ---------- NEXT ---------- */
                    Button(
                        onClick = {
                            currentIndex =
                                (currentIndex + 1) % lessonCards.size
                            showEnglish = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Next")
                    }
                }
            }
        }
    }
}
