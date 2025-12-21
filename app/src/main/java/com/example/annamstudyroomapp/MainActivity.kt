package com.example.annamstudyroomapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


val Context.dataStore by preferencesDataStore(
    name = "user_credentials"
)

val TOKEN = stringPreferencesKey("token")
val EMAIL = stringPreferencesKey("email")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnNamStudyRoomTheme {
                val navigation = rememberNavController()
                val appContext = applicationContext

                val db = AppDatabase.getDatabase(appContext)
//                applicationContext,
//                AppDatabase::class.java, "App Database"
//            ).build()
                val flashCardDao = db.flashCardDao()


                // network
                // Create a single OkHttpClient instance
                val sharedOkHttpClient = OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS) // Set connection timeout
                    .readTimeout(30, TimeUnit.SECONDS)    // Set read timeout
                    .build()

                // 2. Create the first Retrofit instance, using the shared OkHttpClient
                //.client(sharedOkHttpClient) // Pass the shared client

                // Retrofit requires a valid HttpUrl: The baseUrl() method of Retrofit.Builder expects an okhttp3.HttpUrl object.
                // This object represents a well-formed URL and requires a scheme (like "http" or "https"),
                // a host, and optionally a port and path. It cannot be null or an empty string.
                // You can use a placeholder or dummy URL, such as http://localhost/ or http://example.com/,
                // during the initial setup. This satisfies Retrofit's requirement for a valid base URL.

                val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl("https://placeholder.com")
                    .client(sharedOkHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                // Create an implementation of the API endpoints defined by the service interface.
                val networkService = retrofit.create(NetworkService::class.java)

                AnNamStudyRoomApp(navigation, flashCardDao, networkService)
            }
        }
    }
}

@Composable
fun AnNamStudyRoomTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}

//@Preview(showBackground = true)
//@Composable
//fun MenuPagePreview() {
    //AnNamStudyRoomTheme {
        //MenuPage(navController = rememberNavController())
    //}
//}

//@Preview(showBackground = true)
//@Composable
//fun AddCardPagePreview() {
    //AnNamStudyRoomTheme {
        //AddCardPage(navController = rememberNavController())
    //}
//}