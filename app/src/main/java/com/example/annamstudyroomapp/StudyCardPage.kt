package com.example.annamstudyroomapp

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.media3.common.*
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import android.util.Base64
import androidx.compose.runtime.mutableIntStateOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun saveAudioToInternalStorage(context: Context, audioData: ByteArray, filename: String) {
    val file = File(context.filesDir, filename)
    FileOutputStream(file).use { fos ->
        fos.write(audioData)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyCardPage(
    navigateBack: () -> Unit,
    getRandomLesson: suspend (Int) -> List<FlashCard>,
    changeMessage: (String) -> Unit = {},
    networkService: NetworkService
) {
    // List 3 flashcards in lesson
    var lessonCards by remember {mutableStateOf<List<FlashCard>>(emptyList())}
    // Index of the current flashcard
    var currentIndex by remember {mutableIntStateOf(0)}
    // true = current: English, false = current: Vietnamese
    var showEnglish by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val appContext = context.applicationContext
    var audioFile by remember{mutableStateOf("")}
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        changeMessage("Welcome to Study Card Page")
        try {
            val cards = getRandomLesson(2)
            lessonCards = cards
            if (cards.isEmpty()) {
                changeMessage("There are no flash cards in your database.")
            } else {
                // reset trạng thái về card đầu, English first
                currentIndex = 1
                showEnglish = true
                changeMessage("New lesson generated with ${cards.size} cards.")
            }
        } catch (e: Exception) {
            changeMessage("Unexpected error while generating lesson: ${e.message}.")
        }
    }

    LaunchedEffect(currentIndex){
        if (lessonCards.isNotEmpty()) {
            val preferencesFlow: Flow<Preferences> = appContext.dataStore.data
            val preferences = preferencesFlow.first()
            val email = preferences[EMAIL] ?: ""
            val token = preferences[TOKEN] ?: ""
            val word = lessonCards[currentIndex].vietnameseCard.orEmpty()

            scope.launch {
                try {
                    val result = withContext(Dispatchers.IO) {
                        networkService.generateAudio(email = AudioCredential(word, email, token))
                    }
                    if (result.code == 200) {
                        audioFile = "$word.mp3"
                        val audioData: ByteArray = Base64.decode(result.message, Base64.DEFAULT)
                        changeMessage("Audio file ready: $audioFile")
                        saveAudioToInternalStorage(context, audioData ,audioFile)
                    } else {
                        changeMessage("Error: ${result.message}")
                    }
                } catch (e: Exception) {
                    changeMessage("Error: ${e.message}")
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Study card page", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            if (lessonCards.isNotEmpty()) {
                val currentCard = lessonCards[currentIndex]

                val textToShow =
                    if (showEnglish)
                        currentCard.englishCard.orEmpty()
                    else
                        currentCard.vietnameseCard.orEmpty()

                OutlinedButton(
                    onClick = {showEnglish  = !showEnglish},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = textToShow, fontSize = 16.sp)
                }
                if (!showEnglish) {
                    val file = File(context.filesDir, audioFile)
                    val filePath = file.absolutePath
                    // Create a Uri from the file path
                    val uri = filePath.toUri()
                    // Build the MediaItem
                    val mediaItem = MediaItem.fromUri(uri)
                    // Build the Player
                    val player = ExoPlayer.Builder(context).build()
                    player.addListener(object : Player.Listener {
                        override fun onPlaybackStateChanged(playbackState: Int) {
                            when (playbackState) {
                                Player.STATE_BUFFERING -> {
                                    // Player is buffering, show a loading indicator if desired
                                    changeMessage("Buffering...")
                                }

                                Player.STATE_READY -> {
                                    // Player is prepared and ready to play
                                    changeMessage("Ready")
                                }

                                Player.STATE_ENDED -> {
                                    // Playback has finished
                                    player.release()
                                    changeMessage("Finished")
                                }
                                Player.STATE_IDLE -> {
                                    // Player is idle, e.g., after release or error
                                }
                            }
                        }
                    }
                    )

                    Button(
                        onClick = {
                            if(file.exists()){
                                // Set the media item to the player and prepare
                                player.setMediaItem(mediaItem)
                                // Prepare the player.
                                player.prepare()
                                // Start the playback.
                                player.play()
                            }
//                            changeMessage("file path: $file")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Blue
                        )
                    ){
                        Text("Play", fontSize = 16.sp)

                    }

                    Button(
                        onClick = {
                            if (currentIndex == lessonCards.lastIndex){
                                lessonCards = lessonCards.shuffled()
                                currentIndex = 0
                            }
                            else {
                                currentIndex++
                            }
                            showEnglish = true

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Blue
                        )
                    ) {
                        Text("Next", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

