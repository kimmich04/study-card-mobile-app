package com.example.annamstudyroomapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuPage(
    studyCardRoute: () -> Unit,
    addCardRoute: () -> Unit,
    searchCardRoute: () -> Unit,
    loginRoute: () -> Unit,
    changeMessage: (String) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val appContext = LocalContext.current
    changeMessage("Welcome to An Nam Study Room App")

    LaunchedEffect(Unit) {
        //Then, use the DataStore.data property to expose the appropriate stored value using a Flow.

        //In coroutines, a flow is a type that can emit multiple values sequentially,
        //as opposed to suspend functions that return only a single value.
        //For example, you can use a flow to receive live updates from a database.

        //Flows are built on top of coroutines and can provide multiple values.
        //A flow is conceptually a stream of data that can be computed asynchronously.
        //The emitted values must be of the same type. For example, a Flow<Int>
        //is a flow that emits integer values.

        //In Kotlin with Jetpack DataStore, the Flow<Preferences> returned by dataStore.data
        // emits every time any single preference within the DataStore file changes.
        //The flow emits the entire Preferences object, containing all current key-value pairs, with each change.

        //In Kotlin Flow, the first() terminal operator is used to collect only the initial value emitted
        //by a flow and then automatically cancel the flow's execution.
        //This is particularly useful in Jetpack Compose and other Android development scenarios
        //where you only need a single, immediate result from a potentially long-running data stream.
        withContext(Dispatchers.IO){
            val preferencesFlow: Flow<Preferences> = appContext.dataStore.data
            val preferences = preferencesFlow.first()
            val emailValue = preferences[EMAIL] ?: ""
            withContext(Dispatchers.Main) {
                changeMessage(emailValue)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("An Nam Study Room", fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue,
                    titleContentColor = Color.White
                )
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to Your Study Room",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 32.dp)
            )

            MenuCard(
                title = "Study Cards",
                description = "Review your flashcards",
                icon = Icons.Default.Menu,
                onClick = { studyCardRoute() }
            )

            MenuCard(
                title = "Add New Card",
                description = "Create a new flashcard",
                icon = Icons.Default.Add,
                onClick = { addCardRoute() }
            )

            MenuCard(
                title = "Search Card",
                description = "Search your flashcards",
                icon = Icons.Default.Search,
                onClick = { searchCardRoute() }
            )

            MenuCard(
                title = "Log In",
                description = "",
                icon = Icons.Default.Search,
                onClick = { loginRoute() }
            )

            MenuCard(
                title = "Log Out",
                description = "",
                icon = Icons.Default.Search,
                onClick = {
                    scope.launch {
                        appContext.dataStore.edit { preferences ->
                            preferences.remove(EMAIL)
                            preferences.remove(TOKEN)
                            changeMessage(preferences[EMAIL] ?: "")
                        }
                    }
                }

            )
        }
    }
}

@Composable
fun MenuCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(300.dp)
            .height(80.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Blue
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }
    }
}