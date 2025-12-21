package com.example.annamstudyroomapp

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.semantics.contentDescription
//import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import android.database.sqlite.SQLiteConstraintException
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardPage(
    changeMessage: (String) -> Unit = {},
    insertFlashCard: suspend (FlashCard) -> Unit,
    navigateBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val word = remember { mutableStateListOf<Pair<String, String>>() }
    var enWord by rememberSaveable { mutableStateOf("") }
    var vnWord by rememberSaveable { mutableStateOf("") }

    changeMessage("Please add a new flashcard")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Card", fontWeight = FontWeight.Bold) },
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
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Create a New Flashcard",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            OutlinedTextField(
                value = enWord,
                onValueChange = { enWord = it },
                label = { Text("English") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            OutlinedTextField(
                value = vnWord,
                onValueChange = { vnWord = it },
                label = { Text("Vietnamese") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    scope.launch {
                        try {
                            insertFlashCard(
                                FlashCard(
                                    0,
                                    englishCard = enWord,
                                    vietnameseCard = vnWord
                                )
                            )
                            if (vnWord.isNotBlank() && enWord.isNotBlank()) {
                                word.add(enWord to vnWord) //Make a pair of words
                                enWord = "" // Clear the text field
                                vnWord = "" // Clear the text field
                            }
                            changeMessage("Flash card successfully added to your database.")
                        } catch (e: SQLiteConstraintException) {
                            // changeMessage("Flash Cards are duplicated")
                            changeMessage("Flash card already exists in your database.")
                        } catch (e: Exception) {
                            changeMessage("Unexpected Error: ${e.message}")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            ) {
                Text("Save Card", fontSize = 16.sp)
            }

//            Button(
//                onClick = { navigateBack() },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(50.dp),
//                shape = RoundedCornerShape(8.dp),
//                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.Blue
//                )
//            ) {
//                Text("Back", fontSize = 16.sp)
//            }
        }
    }
}