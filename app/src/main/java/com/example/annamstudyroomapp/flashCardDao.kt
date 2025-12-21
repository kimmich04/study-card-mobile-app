package com.example.annamstudyroomapp

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [FlashCard::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun flashCardDao(): FlashCardDao

    companion object {
        @Volatile // Ensures visibility to all threads
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, // Use application context to prevent memory leaks
                    AppDatabase::class.java, "App Database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}


