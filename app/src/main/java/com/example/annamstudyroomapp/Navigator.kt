//Navigator.kt
package com.example.annamstudyroomapp

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.toRoute

@Composable
fun AnNamStudyRoomApp(navController: NavHostController = rememberNavController(), flashCardDao: FlashCardDao, networkService: NetworkService) {
    val navigated: () -> Unit = {
        navController.navigateUp()
    }

    val navigateToHome: () -> Unit = {
        navController.navigate(MainRoute) {
            launchSingleTop = true
        }
    }

    // Define bottom bar messages for each route
    var message by rememberSaveable { mutableStateOf("") }
    val changeMessage: (String) -> Unit = { message = it }

    val toStudy  = { navController.navigate(StudyCardRoute)  { launchSingleTop = true } }
    val toAdd    = { navController.navigate(AddCardRoute)    { launchSingleTop = true } }
    val toSearch = { navController.navigate(SearchCardRoute) { launchSingleTop = true } }
    val toLogIn =  { navController.navigate(LogInRoute)       { launchSingleTop = true } }

    val insertFlashCard: suspend (FlashCard) -> Unit = {
            flashCard -> flashCardDao.insertAll(flashCard)
    }

    val getAllFlashCards: suspend () -> List<FlashCard> = {
        flashCardDao.getAll()
    }

    val getRandomLesson: suspend (Int) -> List<FlashCard> = { limit ->
        flashCardDao.getRandomFlashCards(limit)
    }

    val searchFlashCardByPair: suspend (String, Boolean, String, Boolean) -> List<FlashCard> =
        { en, exactEn, vn, exactVn ->
            flashCardDao.searchFlashCardByPair(
                en,
                exactEn,
                vn,
                exactVn
            )
        }

    val deleteFlashcardByPair: suspend (FlashCard) -> Unit = { card ->
        flashCardDao.deleteByCardPair(
            english = card.englishCard ?: "",
            vietnamese = card.vietnameseCard ?: ""
        )
    }

    val getFlashCardByPair: suspend (String, String) -> FlashCard? = { english, vietnamese ->
        flashCardDao.getFlashCardByPair(english, vietnamese)
    }

    val updateFlashCardByPair: suspend (String, String, String, String, String?) -> Unit =
        { englishOld, vietnameseOld, englishNew, vietnameseNew, audioNew ->
            flashCardDao.updateFlashCardByPair(
                englishOld,
                vietnameseOld,
                englishNew,
                vietnameseNew,
                audioNew
                )
        }

    val onEditCard: (FlashCard) -> Unit = { card ->
        navController.navigate(
            EditCardRoute(
                engOld = card.englishCard ?: "",
                vietOld = card.vietnameseCard ?: ""
            )
        )
    }

    val onCardSelected: (FlashCard) -> Unit = { card ->
        navController.navigate(
            ShowCardRoute(
                english = card.englishCard ?: "",
                vietnamese = card.vietnameseCard ?: ""
            )
        )
    }

    val onClickEmail: (String) -> Unit = { email ->
        navController.navigate(
            TokenRoute(
                email
            )
        ){ launchSingleTop = true }
    }


    Scaffold(
        bottomBar = {
            BottomAppBar( // Don't show the bottom bar if the route is null or empty
                actions = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = message
                    )
                },
                containerColor = Color.Blue,
                contentColor = Color.White
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            MainRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<MainRoute> {
                MenuScreen(
                    studyCardRoute = toStudy,
                    addCardRoute = toAdd,
                    searchCardRoute = toSearch,
                    loginRoute = toLogIn,
                    changeMessage = changeMessage
                )
            }
            composable<AddCardRoute> {
                AddCardScreen(
                    changeMessage = changeMessage,
                    insertFlashCard = insertFlashCard,
                    navigateBack = navigated
                )
            }

            composable<StudyCardRoute> {
                StudyCardScreen(
                    navigateBack = navigated,
                    getRandomLesson = getRandomLesson,
                    updateFlashCardByPair = updateFlashCardByPair,
                    changeMessage = changeMessage,
                    networkService = networkService,
                )
            }

            composable<SearchCardRoute> {
                SearchCardScreen(
                    changeMessage = changeMessage,
                    getAllFlashCards = getAllFlashCards,
                    navigateBack = navigated,
                    searchFlashCardByPair = searchFlashCardByPair,
                    deleteFlashcardByPair = deleteFlashcardByPair,
                    onEditCard = onEditCard,
                    onSelectedCard = onCardSelected
                )
            }

            composable<EditCardRoute> {backStackEntry ->
                val args: EditCardRoute = backStackEntry.toRoute()
                EditCardScreen(
                    changeMessage = changeMessage,
                    navigateBack = navigated,
                    englishOld = args.engOld,
                    vietnameseOld = args.vietOld,
                    getFlashCardByPair = getFlashCardByPair,
                    updateFlashCardByPair = updateFlashCardByPair,
                    networkService = networkService
                )
            }

            composable<ShowCardRoute> { backStackEntry ->
                val args: ShowCardRoute = backStackEntry.toRoute()
                ShowCardScreen(
                    changeMessage = changeMessage,
                    navigateBack = navigated,
                    english = args.english,
                    vietnamese = args.vietnamese,
                    getAllFlashCards = getAllFlashCards
                )
            }

            composable<LogInRoute> {
                LogInScreen(
                    changeMessage = changeMessage,
                    navigateBack = navigated,
                    networkService = networkService,
                    onClickEmail = onClickEmail
                )
            }

            composable<TokenRoute> {
                    backStackEntry ->
                val args: TokenRoute = backStackEntry.toRoute()
                TokenScreen(
                    email = args.email,
                    changeMessage = changeMessage,
                    navigateToHome = navigateToHome
                )
            }
        }
    }
}