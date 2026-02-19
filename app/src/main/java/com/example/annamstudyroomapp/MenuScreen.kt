//MenuScreen.kt
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
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    studyCardRoute: () -> Unit,
    addCardRoute: () -> Unit,
    searchCardRoute: () -> Unit,
    loginRoute: () -> Unit,
    //logoutRoute: () -> Unit,
    changeMessage: (String) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val appContext = LocalContext.current
    changeMessage("Welcome to An Nam Study Room App")

    LaunchedEffect(Unit) {
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
                title = { Text("An Nam Study Room", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Welcome to Your Study Room",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 32.dp)
            )

            // 🔹 MAIN MENU CARDS (these were never wrong)
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MenuCard(
                    title = "Study Cards",
                    //description = "Review your flashcards",
                    description = null,
                    //icon = Icons.Default.Menu,
                    icon = null,
                    onClick = studyCardRoute
                )

                MenuCard(
                    title = "Add New Card",
                    //description = "Create a new flashcard",
                    description = null,
                    //icon = Icons.Default.Add,
                    icon = null,
                    onClick = addCardRoute
                )

                MenuCard(
                    title = "Search Card",
                    //description = "Search your flashcards",
                    description = null,
                    //icon = Icons.Default.Search,
                    icon = null,
                    onClick = searchCardRoute
                )

                MenuCard(
                    title = "Log In",
                    description = null,
                    icon = null,
                    onClick = loginRoute
                )

                MenuCard(
                    title = "Log Out",
                    description = null,
                    icon = null,
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
}

@Composable
fun MenuCard(
    title: String,
    description: String? = null,
    icon: ImageVector? = null,
    onClick: () -> Unit
) {
    val isSimple = description == null && icon == null

    Card(
        onClick = onClick,
        modifier = Modifier
            .width(if (isSimple) 200.dp else 300.dp)
            .height(if (isSimple) 48.dp else 80.dp),
        shape = RoundedCornerShape(if (isSimple) 24.dp else 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Blue
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (isSimple)
                Arrangement.Center
            else
                Arrangement.Start
        ) {

            // 🔹 Icon only for full cards
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(16.dp))
            }

            Column(
                horizontalAlignment = if (isSimple)
                    Alignment.CenterHorizontally
                else
                    Alignment.Start
            ) {
                Text(
                    text = title,
                    fontSize = if (isSimple) 16.sp else 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // 🔹 Description only for full cards
                if (description != null) {
                    Text(
                        text = description,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}