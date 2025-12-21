package com.example.annamstudyroomapp

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.semantics.contentDescription
//import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.*



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogIn(
    navigateBack: () -> Unit,
    networkService: NetworkService,
    changeMessage: (String) -> Unit = {},
    onClickEmail: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        changeMessage("Please enter your email and get your Token")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Log in interface", fontWeight = FontWeight.Bold) },
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
                text = "Please LogIn",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Your email") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    scope.launch {
                        try{
                            val result = withContext(Dispatchers.IO) {
                                networkService.generateToken(email = UserCredential(email))
                            }
                            if (result.code == 200) {
                                onClickEmail(email)
                            }
                            else {
                                changeMessage(result.message)
                            }
                        }catch (e: Exception) {
                            changeMessage("Error: ${e.message}")
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
                Text("Verify email", fontSize = 16.sp)
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

