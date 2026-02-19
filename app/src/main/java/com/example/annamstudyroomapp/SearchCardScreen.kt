//SearchCardScreen.kt
package com.example.annamstudyroomapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.material3.Checkbox


@Composable
fun FlashCardList(
    flashCards: List<FlashCard>,
    selectedItem: (FlashCard) -> Unit,
    editItem: (FlashCard) -> Unit,
    onDelete: (FlashCard) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = flashCards,
            key = { it.uid }
        ) { card ->

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedItem(card) }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // TEXT COLUMN
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = card.englishCard.orEmpty(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = card.vietnameseCard.orEmpty(),
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                            // Actions close to the Vietnamese text
                            IconButton(
                                onClick = { editItem(card) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Blue)
                            }
                            IconButton(
                                onClick = { onDelete(card) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCardScreen(
    navigateBack: () -> Unit,
    getAllFlashCards: suspend () -> List<FlashCard>,
    changeMessage: (String) -> Unit = {},
    searchFlashCardByPair: suspend (String, Boolean, String, Boolean) -> List<FlashCard>,
    deleteFlashcardByPair: suspend (FlashCard) -> Unit,
    onEditCard: (FlashCard) -> Unit,
    onSelectedCard: (FlashCard) -> Unit
) {
    var flashCards by remember { mutableStateOf<List<FlashCard>>(emptyList()) }

    var enText by rememberSaveable { mutableStateOf("") }
    var vnText by rememberSaveable { mutableStateOf("") }

    var exactEn by rememberSaveable { mutableStateOf(false) }
    var exactVn by rememberSaveable { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        flashCards = getAllFlashCards()
        changeMessage("Welcome to Search Card Screen")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Card Page", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            /* -------- ENGLISH SEARCH -------- */
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(
                    checked = exactEn,
                    onCheckedChange = { exactEn = it }
                )

                OutlinedTextField(
                    value = enText,
                    onValueChange = { enText = it },
                    label = { Text("English") },
                    modifier = Modifier.weight(1f)
                )
            }

            /* -------- VIETNAMESE SEARCH -------- */
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(
                    checked = exactVn,
                    onCheckedChange = { exactVn = it }
                )

                OutlinedTextField(
                    value = vnText,
                    onValueChange = { vnText = it },
                    label = { Text("Vietnamese") },
                    modifier = Modifier.weight(1f)
                )
            }

            Button(
                onClick = {
                    scope.launch {
                        flashCards = searchFlashCardByPair(enText, exactEn, vnText, exactVn)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Search")
            }

            FlashCardList(
                flashCards = flashCards,
                selectedItem = onSelectedCard,
                editItem = onEditCard,
                onDelete = { card ->
                    scope.launch {
                        deleteFlashcardByPair(card)
                        flashCards = searchFlashCardByPair(enText, exactEn, vnText, exactVn)
                    }
                }
            )
        }
    }
}


