package com.example.annamstudyroomapp

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.testing.TestNavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.test.core.app.ApplicationProvider
import androidx.compose.ui.test.hasText
import androidx.navigation.Navigator
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals

class MenuScreenUITest {
    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Test 1: Verify that "Add New Card" button exists in the Menu screen
     */
    @Test
    fun menuScreen_addCardButton_exists() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        navController.navigatorProvider.addNavigator(ComposeNavigator())

        // Given: Menu screen is displayed
        composeTestRule.setContent {
            AnNamStudyRoomTheme {
                //val navController = rememberNavController()
                //MenuPage(navController = navController)
                AnNamStudyRoomApp(navController)
            }

            assertEquals("menu", navController.currentDestination?.route)
        }

        // Then: "Add New Card" card/button should be displayed
        composeTestRule
            .onNodeWithText("Add New Card")
            .assertIsDisplayed()
    }

    /**
     * Test 2: Verify navigation flow from Menu -> Add Card -> Back -> Menu
     * When clicking "Add New Card", the AddCardPage appears with a Back button,
     * and clicking Back returns to Menu with "Add New Card" button visible
     */
    @Test
    fun menuScreen_clickAddCard_navigatesToAddCardPage_andBackReturnsToMenu() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        navController.navigatorProvider.addNavigator(ComposeNavigator())
        // Given: The app starts with the full navigation host
        composeTestRule.setContent {
            AnNamStudyRoomTheme {
                AnNamStudyRoomApp(navController)
                //.assertEquals("MenuPage", navController.currentDestination?.route)
            }
        }

        // Then: AddCardPage should be displayed with its title
        composeTestRule
            .onNodeWithText("Add New Card")
            .assertIsDisplayed()

        //When: Click on "Add New Card" card in Menu screen
        composeTestRule
            .onNodeWithText("Add New Card")
            .performClick()
        assertEquals("add_card", navController.currentDestination?.route)
        // And: Back button (arrow) should be displayed
        composeTestRule
            .onNodeWithContentDescription("Back")
            .assertIsDisplayed()

        // When: Click the Back button
        composeTestRule
            .onNodeWithContentDescription("Back")
            .performClick()

        // Then: Should return to Menu screen with "Add New Card" button visible
        composeTestRule
            .onNodeWithText("Add New Card")
            .assertIsDisplayed()

        // And: Menu screen title should be displayed
        composeTestRule
            .onNodeWithText("An Nam Study Room")
            .assertIsDisplayed()
    }

    /**
     * Test 3: Verify the complete Add Card screen elements are present
     */
    @Test
    fun addCardScreen_allElements_areDisplayed() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        navController.navigatorProvider.addNavigator(ComposeNavigator())
        // Given: Navigate to Add Card screen
        composeTestRule.setContent {
            AnNamStudyRoomTheme {
                AnNamStudyRoomApp(navController)
            }
        }

        // When: Navigate to Add Card page
        composeTestRule
            .onNodeWithText("Add New Card")
            .performClick()

        // Then: All elements should be displayed
        composeTestRule
            .onNodeWithText("Create a New Flashcard")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("English")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Vietnamese")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Save Card")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Back")
            .assertIsDisplayed()
    }

    private lateinit var navController: TestNavHostController
    private val getUserDouble = { _: String, _: String -> "some-user" }
    private val writeCredentialsDouble = { _: String, _: String, _: String, _: String -> }
    private val makeRequestWithCredentialsSuccessDouble = { _: String, _: () -> Unit, _: () -> Unit -> }
    @Test
    fun displayMessage() {
        composeTestRule.setContent {
            navController = TestNavHostController(ApplicationProvider.getApplicationContext())
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            AnNamStudyRoomApp(
                navController,
            )
        }

        // The app starts on the Menu screen, which has the "Message" content description.
        // There's no need to navigate to another screen for this test.

        composeTestRule.onNodeWithContentDescription("Message")
            .assertExists()
            .assert(hasText("please select an option"))
    }
}

