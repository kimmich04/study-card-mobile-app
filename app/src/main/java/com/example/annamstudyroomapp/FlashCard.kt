package com.example.annamstudyroomapp

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase


@Entity(tableName = "FlashCards", indices = [Index(value = ["english_card", "vietnamese_card"],
    unique = true)])
data class FlashCard(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "english_card") val englishCard: String?,
    @ColumnInfo(name = "vietnamese_card") val vietnameseCard: String?
)

@Dao
interface FlashCardDao {
    @Query("SELECT * FROM FlashCards ORDER BY english_card ASC")
    suspend fun getAll(): List<FlashCard>

//    Load all Flashcard using IDs
    @Query("SELECT * FROM FlashCards WHERE uid IN (:flashCardIds)")
    suspend fun loadAllByIds(flashCardIds: IntArray): List<FlashCard>

//    Take Flashcard with "LIKE"
    @Query("SELECT * FROM FlashCards WHERE english_card LIKE :english AND " +
            "vietnamese_card LIKE :vietnamese LIMIT 1")
    suspend fun findByCards(english: String, vietnamese: String): FlashCard

// take Flashcard by pair
    @Query("SELECT * FROM FlashCards " +
                "WHERE english_card = :english AND vietnamese_card = :vietnamese " +
                "LIMIT 1")
    suspend fun getFlashCardByPair(english: String, vietnamese: String): FlashCard?

//    Delete Flashcard by pair
    @Query("DELETE FROM FlashCards " +
                "WHERE english_card = :english AND vietnamese_card = :vietnamese")
    suspend fun deleteByCardPair(english: String, vietnamese: String)

//    Select Flashcard by pair
    @Query("SELECT * FROM FlashCards " +
            "WHERE english_card LIKE '%' || :english || '%' " +
            "AND vietnamese_card LIKE '%' || :vietnamese || '%' ")
    suspend fun searchFlashCardByPair(english: String, vietnamese: String): List<FlashCard>

    @Query("UPDATE FlashCards " +
            "SET english_card = :englishNew, " +
            "vietnamese_card = :vietnameseNew " +
            "WHERE english_card = :englishOld " +
            "AND vietnamese_card = :vietnameseOld")
    suspend fun updateFlashCardByPair( englishOld: String, vietnameseOld: String, englishNew: String,  vietnameseNew: String )

    @Insert
    suspend fun insertAll(vararg flashCard: FlashCard)

//    @Delete
//    suspend fun delete(flashCard: FlashCard)

    @Query("SELECT * FROM FlashCards ORDER BY RANDOM() LIMIT :size")
    suspend fun getRandomFlashCards(size: Int): List<FlashCard>


}