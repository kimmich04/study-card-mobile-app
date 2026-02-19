//EditCardScreen.kt
package com.example.annamstudyroomapp

import android.util.Base64
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest

/* ---------- Utils ---------- */

fun generateAudioKey(text: String): String {
    val bytes = MessageDigest
        .getInstance("SHA-256")
        .digest(text.trim().lowercase().toByteArray())

    return bytes.joinToString("") { "%02x".format(it) }
}

/* ---------- Screen ---------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCardScreen(
    navigateBack: () -> Unit,
    changeMessage: (String) -> Unit,
    englishOld: String,
    vietnameseOld: String,
    getFlashCardByPair: suspend (String, String) -> FlashCard?,
    updateFlashCardByPair: suspend (String, String, String, String, String) -> Unit,
    networkService: NetworkService
) {
    val context = LocalContext.current
    val appContext = context.applicationContext
    val scope = rememberCoroutineScope()

    var englishText by remember { mutableStateOf("") }
    var vietnameseText by remember { mutableStateOf("") }
    var audioKey by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    /* -------- Player -------- */
    val player = remember { ExoPlayer.Builder(context).build() }
    DisposableEffect(Unit) { onDispose { player.release() } }

    /* -------- Load card -------- */
    LaunchedEffect(Unit) {
        val card = getFlashCardByPair(englishOld, vietnameseOld)
        card?.let {
            englishText = it.englishCard.orEmpty()
            vietnameseText = it.vietnameseCard.orEmpty()
            audioKey = it.audioCard.orEmpty()
        }
    }

    val audioExists = audioKey.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit card", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue,
                    titleContentColor = Color.White
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

            OutlinedTextField(
                value = englishText,
                onValueChange = { englishText = it },
                label = { Text("English") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = vietnameseText,
                onValueChange = { vietnameseText = it },
                label = { Text("Vietnamese") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = audioKey,
                onValueChange = {},
                enabled = false,
                label = { Text("Audio key") },
                modifier = Modifier.fillMaxWidth()
            )

            /* ---------- UPDATE ---------- */
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    scope.launch {
                        updateFlashCardByPair(
                            englishOld,
                            vietnameseOld,
                            englishText,
                            vietnameseText,
                            audioKey
                        )
                        changeMessage("Card updated")
                        navigateBack()
                    }
                }
            ) {
                Text("Update")
            }

            /* ---------- GENERATE ---------- */
            if (!audioExists) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    onClick = {
                        scope.launch {
                            val prefs = appContext.dataStore.data.first()
                            val email = prefs[EMAIL].orEmpty()
                            val token = prefs[TOKEN].orEmpty()

                            if (email.isBlank() || token.isBlank()) {
                                changeMessage("User not logged in")
                                return@launch
                            }

                            isLoading = true
                            val key = generateAudioKey(vietnameseText)
                            audioKey = key

                            try {
                                val result = withContext(Dispatchers.IO) {
                                    networkService.generateAudio(
                                        email = AudioCredential(
                                            word = vietnameseText,
                                            email = email,
                                            token = token
                                        )
                                    )
                                }

                                if (result.code == 200) {
                                    val bytes =
                                        Base64.decode(result.message, Base64.DEFAULT)

                                    saveAudioToInternalStorage(
                                        context,
                                        bytes,
                                        "$key.mp3"
                                    )
                                    changeMessage("Audio generated")
                                } else {
                                    audioKey = ""
                                    changeMessage("Generation failed")
                                }
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                ) {
                    Text(if (isLoading) "Generating..." else "Generate audio")
                }
            }

            /* ---------- PLAY + CLEAN ---------- */
            if (audioExists) {

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val file = File(context.filesDir, "$audioKey.mp3")
                        if (!file.exists()) {
                            changeMessage("Audio file missing")
                            return@Button
                        }

                        player.setMediaItem(MediaItem.fromUri(file.toUri()))
                        player.prepare()
                        player.play()
                    }
                ) {
                    Text("Play audio")
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val file = File(context.filesDir, "$audioKey.mp3")
                        if (file.exists()) file.delete()

                        audioKey = ""
                        changeMessage("Audio cleaned (click Update to save)")
                    }
                ) {
                    Text("Clean audio")
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
