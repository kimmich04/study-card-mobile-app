package com.example.annamstudyroomapp

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TokenScreen(
    email: String,
    changeMessage: (String) -> Unit,
    navigateToHome: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val appContext = context.applicationContext
    var token by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        changeMessage("Please, introduce your token.")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Token Page", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
    ){paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = token,
                onValueChange = { token = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "tokenTextField" },
                label = { Text("token") },
                minLines = 3
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .semantics { contentDescription = "Enter" },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                onClick = {
                    scope.launch {
                        //The withContext function is your primary tool for seamlessly moving between Dispatchers.IO,
                        //Dispatchers.Default,
                        //and Dispatchers.Main within a single coroutine, ensuring background tasks don't freeze the UI.

                        //Start on Main (Implicitly): Composable functions generally run on the Main thread.
                        //Switch to IO: Use withContext(Dispatchers.IO) { ... } to perform heavy lifting (database, network)
                        // without blocking the UI.
                        //Switch back to Main: After the withContext(Dispatchers.IO) block finishes,
                        // the coroutine automatically resumes on the original Main dispatcher
                        // where you can update your UI state and trigger recomposition.

                        withContext(Dispatchers.IO) {
                            //token = result.token
                            //Prefer ApplicationContext: When you need a Context for operations that do not interact with the UI
                            //(e.g., file operations, database access, accessing resources like strings or drawables),
                            // use the application context.
                            //The application context lives for the lifetime of your app and is safe to use on any thread.

                            appContext.dataStore.edit { preferences ->
                                preferences[EMAIL] = email
                                preferences[TOKEN] = token
                            }
                        }
                        navigateToHome()
                    }
                })
            {
                Text("Enter")
            }
        }
    }
}
