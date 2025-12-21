package com.example.annamstudyroomapp

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashCardList(
    editItem: (FlashCard) -> Unit,
    selectedItem: (FlashCard) -> Unit,
    flashCards: List<FlashCard>,
    onDelete: (FlashCard) -> Unit
) {

    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        items(
            items = flashCards,
            key = { flashCard -> flashCard.uid }
        ) { flashCard ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = Color.LightGray)
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedItem(flashCard) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = flashCard.englishCard.orEmpty(),
                        modifier = Modifier.padding(6.dp)
                    )
                    Text(
                        text = " = ",
                        modifier = Modifier.padding(6.dp)
                    )
                    Text(
                        text = flashCard.vietnameseCard.orEmpty(),
                        modifier = Modifier.padding(6.dp)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Edit",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { editItem(flashCard) }
                            .padding(4.dp),
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    )

                    Text(
                        text = "Delete",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { onDelete(flashCard) }
                            .padding(4.dp), // cho dễ bấm hơn
                        // có thể thêm style cho giống link hoặc nút
                        color = MaterialTheme.colorScheme.error,
                        textDecoration = TextDecoration.Underline
                    )


                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCardPage(
    navigateBack: () -> Unit,
    changeMessage: (String) -> Unit = {},
    getAllFlashCards: suspend () -> List<FlashCard>,
    searchFlashcardByPair: suspend (String, String) -> List<FlashCard>,
    deleteFlashcardByPair: suspend (FlashCard) -> Unit,
    onEditCard: (FlashCard) -> Unit,
    onSelectedCard: (FlashCard) -> Unit
) {
    var flashCards: List<FlashCard> by remember { mutableStateOf(emptyList()) }
    var vnText by rememberSaveable { mutableStateOf("") }
    var enText by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val updateCardList: suspend () -> Unit = {
        flashCards = searchFlashcardByPair(enText, vnText)
    }

    LaunchedEffect(Unit) {
        flashCards = getAllFlashCards()
        changeMessage("Welcome to Study Card Screen")
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

            FlashCardList(
                flashCards = flashCards,
                selectedItem =  onSelectedCard,
                editItem = onEditCard,
                onDelete = { card ->
                    scope.launch {
                        deleteFlashcardByPair(card)
                        updateCardList()
                    }
                }
            )
        }
    }
}