package com.example.annamstudyroomapp

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
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCardScreen(
    navigateBack: () -> Unit,
    changeMessage: (String) -> Unit = {},
    englishOld: String,
    vietnameseOld: String,
    getFlashcardByPair: suspend (String, String) -> FlashCard?,
    updateFlashcardByPair: suspend (String, String, String, String) -> Unit
) {
    var card by remember { mutableStateOf<FlashCard?>(null) }

    val scope = rememberCoroutineScope()

    var englishText by remember { mutableStateOf("") }
    var vietnameseText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        changeMessage("Welcome to Edit Card Page")
    }

    LaunchedEffect(englishOld, vietnameseOld) {
        val loaded = getFlashcardByPair(englishOld, vietnameseOld)
        card = loaded
        if (loaded != null) {
            englishText = loaded.englishCard ?: ""
            vietnameseText = loaded.vietnameseCard ?: ""
        }
    }

    if (card == null) {
        Text("Loading...")
        return
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
            Text(
                text = "Edit Flashcard",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            OutlinedTextField (
                value = englishText,
                onValueChange = {englishText = it},
                enabled = true,
                label = { Text(stringResource(R.string.english_label))},
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter text") }
            )

            OutlinedTextField (
                value = vietnameseText,
                onValueChange = {vietnameseText = it},
                enabled = true,
                label = { Text(stringResource(R.string.vietnamese_label))},
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter text") }
            )

            Button(
                onClick = {
                    scope.launch {
                        updateFlashcardByPair(
                            englishOld,
                            vietnameseOld,
                            englishText,
                            vietnameseText)
                        changeMessage("Card updated")
                        navigateBack()
                    }
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
                Text("Update")
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}