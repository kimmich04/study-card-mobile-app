package com.example.annamstudyroomapp

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.appendPlaceholders
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.IntArray
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlin.text.StringBuilder

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class FlashCardDao_Impl(
  __db: RoomDatabase,
) : FlashCardDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfFlashCard: EntityInsertAdapter<FlashCard>
  init {
    this.__db = __db
    this.__insertAdapterOfFlashCard = object : EntityInsertAdapter<FlashCard>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `FlashCards` (`uid`,`english_card`,`vietnamese_card`,`audio_card`) VALUES (nullif(?, 0),?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: FlashCard) {
        statement.bindLong(1, entity.uid.toLong())
        val _tmpEnglishCard: String? = entity.englishCard
        if (_tmpEnglishCard == null) {
          statement.bindNull(2)
        } else {
          statement.bindText(2, _tmpEnglishCard)
        }
        val _tmpVietnameseCard: String? = entity.vietnameseCard
        if (_tmpVietnameseCard == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpVietnameseCard)
        }
        val _tmpAudioCard: String? = entity.audioCard
        if (_tmpAudioCard == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpAudioCard)
        }
      }
    }
  }

  public override suspend fun insertAll(vararg flashCard: FlashCard): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfFlashCard.insert(_connection, flashCard)
  }

  public override suspend fun getAll(): List<FlashCard> {
    val _sql: String = "SELECT * FROM FlashCards ORDER BY english_card ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfUid: Int = getColumnIndexOrThrow(_stmt, "uid")
        val _columnIndexOfEnglishCard: Int = getColumnIndexOrThrow(_stmt, "english_card")
        val _columnIndexOfVietnameseCard: Int = getColumnIndexOrThrow(_stmt, "vietnamese_card")
        val _columnIndexOfAudioCard: Int = getColumnIndexOrThrow(_stmt, "audio_card")
        val _result: MutableList<FlashCard> = mutableListOf()
        while (_stmt.step()) {
          val _item: FlashCard
          val _tmpUid: Int
          _tmpUid = _stmt.getLong(_columnIndexOfUid).toInt()
          val _tmpEnglishCard: String?
          if (_stmt.isNull(_columnIndexOfEnglishCard)) {
            _tmpEnglishCard = null
          } else {
            _tmpEnglishCard = _stmt.getText(_columnIndexOfEnglishCard)
          }
          val _tmpVietnameseCard: String?
          if (_stmt.isNull(_columnIndexOfVietnameseCard)) {
            _tmpVietnameseCard = null
          } else {
            _tmpVietnameseCard = _stmt.getText(_columnIndexOfVietnameseCard)
          }
          val _tmpAudioCard: String?
          if (_stmt.isNull(_columnIndexOfAudioCard)) {
            _tmpAudioCard = null
          } else {
            _tmpAudioCard = _stmt.getText(_columnIndexOfAudioCard)
          }
          _item = FlashCard(_tmpUid,_tmpEnglishCard,_tmpVietnameseCard,_tmpAudioCard)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun loadAllByIds(flashCardIds: IntArray): List<FlashCard> {
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("SELECT * FROM FlashCards WHERE uid IN (")
    val _inputSize: Int = flashCardIds.size
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        for (_item: Int in flashCardIds) {
          _stmt.bindLong(_argIndex, _item.toLong())
          _argIndex++
        }
        val _columnIndexOfUid: Int = getColumnIndexOrThrow(_stmt, "uid")
        val _columnIndexOfEnglishCard: Int = getColumnIndexOrThrow(_stmt, "english_card")
        val _columnIndexOfVietnameseCard: Int = getColumnIndexOrThrow(_stmt, "vietnamese_card")
        val _columnIndexOfAudioCard: Int = getColumnIndexOrThrow(_stmt, "audio_card")
        val _result: MutableList<FlashCard> = mutableListOf()
        while (_stmt.step()) {
          val _item_1: FlashCard
          val _tmpUid: Int
          _tmpUid = _stmt.getLong(_columnIndexOfUid).toInt()
          val _tmpEnglishCard: String?
          if (_stmt.isNull(_columnIndexOfEnglishCard)) {
            _tmpEnglishCard = null
          } else {
            _tmpEnglishCard = _stmt.getText(_columnIndexOfEnglishCard)
          }
          val _tmpVietnameseCard: String?
          if (_stmt.isNull(_columnIndexOfVietnameseCard)) {
            _tmpVietnameseCard = null
          } else {
            _tmpVietnameseCard = _stmt.getText(_columnIndexOfVietnameseCard)
          }
          val _tmpAudioCard: String?
          if (_stmt.isNull(_columnIndexOfAudioCard)) {
            _tmpAudioCard = null
          } else {
            _tmpAudioCard = _stmt.getText(_columnIndexOfAudioCard)
          }
          _item_1 = FlashCard(_tmpUid,_tmpEnglishCard,_tmpVietnameseCard,_tmpAudioCard)
          _result.add(_item_1)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun findByCards(english: String, vietnamese: String): FlashCard {
    val _sql: String = "SELECT * FROM FlashCards WHERE english_card LIKE ? AND vietnamese_card LIKE ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, english)
        _argIndex = 2
        _stmt.bindText(_argIndex, vietnamese)
        val _columnIndexOfUid: Int = getColumnIndexOrThrow(_stmt, "uid")
        val _columnIndexOfEnglishCard: Int = getColumnIndexOrThrow(_stmt, "english_card")
        val _columnIndexOfVietnameseCard: Int = getColumnIndexOrThrow(_stmt, "vietnamese_card")
        val _columnIndexOfAudioCard: Int = getColumnIndexOrThrow(_stmt, "audio_card")
        val _result: FlashCard
        if (_stmt.step()) {
          val _tmpUid: Int
          _tmpUid = _stmt.getLong(_columnIndexOfUid).toInt()
          val _tmpEnglishCard: String?
          if (_stmt.isNull(_columnIndexOfEnglishCard)) {
            _tmpEnglishCard = null
          } else {
            _tmpEnglishCard = _stmt.getText(_columnIndexOfEnglishCard)
          }
          val _tmpVietnameseCard: String?
          if (_stmt.isNull(_columnIndexOfVietnameseCard)) {
            _tmpVietnameseCard = null
          } else {
            _tmpVietnameseCard = _stmt.getText(_columnIndexOfVietnameseCard)
          }
          val _tmpAudioCard: String?
          if (_stmt.isNull(_columnIndexOfAudioCard)) {
            _tmpAudioCard = null
          } else {
            _tmpAudioCard = _stmt.getText(_columnIndexOfAudioCard)
          }
          _result = FlashCard(_tmpUid,_tmpEnglishCard,_tmpVietnameseCard,_tmpAudioCard)
        } else {
          error("The query result was empty, but expected a single row to return a NON-NULL object of type 'com.example.annamstudyroomapp.FlashCard'.")
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getFlashCardByPair(english: String, vietnamese: String): FlashCard? {
    val _sql: String = "SELECT * FROM FlashCards WHERE english_card = ? AND vietnamese_card = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, english)
        _argIndex = 2
        _stmt.bindText(_argIndex, vietnamese)
        val _columnIndexOfUid: Int = getColumnIndexOrThrow(_stmt, "uid")
        val _columnIndexOfEnglishCard: Int = getColumnIndexOrThrow(_stmt, "english_card")
        val _columnIndexOfVietnameseCard: Int = getColumnIndexOrThrow(_stmt, "vietnamese_card")
        val _columnIndexOfAudioCard: Int = getColumnIndexOrThrow(_stmt, "audio_card")
        val _result: FlashCard?
        if (_stmt.step()) {
          val _tmpUid: Int
          _tmpUid = _stmt.getLong(_columnIndexOfUid).toInt()
          val _tmpEnglishCard: String?
          if (_stmt.isNull(_columnIndexOfEnglishCard)) {
            _tmpEnglishCard = null
          } else {
            _tmpEnglishCard = _stmt.getText(_columnIndexOfEnglishCard)
          }
          val _tmpVietnameseCard: String?
          if (_stmt.isNull(_columnIndexOfVietnameseCard)) {
            _tmpVietnameseCard = null
          } else {
            _tmpVietnameseCard = _stmt.getText(_columnIndexOfVietnameseCard)
          }
          val _tmpAudioCard: String?
          if (_stmt.isNull(_columnIndexOfAudioCard)) {
            _tmpAudioCard = null
          } else {
            _tmpAudioCard = _stmt.getText(_columnIndexOfAudioCard)
          }
          _result = FlashCard(_tmpUid,_tmpEnglishCard,_tmpVietnameseCard,_tmpAudioCard)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun searchFlashCardByPair(
    en: String,
    exactEn: Boolean,
    vn: String,
    exactVn: Boolean,
  ): List<FlashCard> {
    val _sql: String = "SELECT * FROM FlashCards WHERE (CASE WHEN ? THEN english_card LIKE ?  WHEN NOT ?  THEN english_card LIKE '%' || ? || '%' END) AND (CASE WHEN ? THEN vietnamese_card LIKE ? WHEN NOT ? THEN vietnamese_card LIKE '%' || ? || '%' END)"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: Int = if (exactEn) 1 else 0
        _stmt.bindLong(_argIndex, _tmp.toLong())
        _argIndex = 2
        _stmt.bindText(_argIndex, en)
        _argIndex = 3
        val _tmp_1: Int = if (exactEn) 1 else 0
        _stmt.bindLong(_argIndex, _tmp_1.toLong())
        _argIndex = 4
        _stmt.bindText(_argIndex, en)
        _argIndex = 5
        val _tmp_2: Int = if (exactVn) 1 else 0
        _stmt.bindLong(_argIndex, _tmp_2.toLong())
        _argIndex = 6
        _stmt.bindText(_argIndex, vn)
        _argIndex = 7
        val _tmp_3: Int = if (exactVn) 1 else 0
        _stmt.bindLong(_argIndex, _tmp_3.toLong())
        _argIndex = 8
        _stmt.bindText(_argIndex, vn)
        val _columnIndexOfUid: Int = getColumnIndexOrThrow(_stmt, "uid")
        val _columnIndexOfEnglishCard: Int = getColumnIndexOrThrow(_stmt, "english_card")
        val _columnIndexOfVietnameseCard: Int = getColumnIndexOrThrow(_stmt, "vietnamese_card")
        val _columnIndexOfAudioCard: Int = getColumnIndexOrThrow(_stmt, "audio_card")
        val _result: MutableList<FlashCard> = mutableListOf()
        while (_stmt.step()) {
          val _item: FlashCard
          val _tmpUid: Int
          _tmpUid = _stmt.getLong(_columnIndexOfUid).toInt()
          val _tmpEnglishCard: String?
          if (_stmt.isNull(_columnIndexOfEnglishCard)) {
            _tmpEnglishCard = null
          } else {
            _tmpEnglishCard = _stmt.getText(_columnIndexOfEnglishCard)
          }
          val _tmpVietnameseCard: String?
          if (_stmt.isNull(_columnIndexOfVietnameseCard)) {
            _tmpVietnameseCard = null
          } else {
            _tmpVietnameseCard = _stmt.getText(_columnIndexOfVietnameseCard)
          }
          val _tmpAudioCard: String?
          if (_stmt.isNull(_columnIndexOfAudioCard)) {
            _tmpAudioCard = null
          } else {
            _tmpAudioCard = _stmt.getText(_columnIndexOfAudioCard)
          }
          _item = FlashCard(_tmpUid,_tmpEnglishCard,_tmpVietnameseCard,_tmpAudioCard)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getRandomFlashCards(size: Int): List<FlashCard> {
    val _sql: String = "SELECT * FROM FlashCards ORDER BY RANDOM() LIMIT ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, size.toLong())
        val _columnIndexOfUid: Int = getColumnIndexOrThrow(_stmt, "uid")
        val _columnIndexOfEnglishCard: Int = getColumnIndexOrThrow(_stmt, "english_card")
        val _columnIndexOfVietnameseCard: Int = getColumnIndexOrThrow(_stmt, "vietnamese_card")
        val _columnIndexOfAudioCard: Int = getColumnIndexOrThrow(_stmt, "audio_card")
        val _result: MutableList<FlashCard> = mutableListOf()
        while (_stmt.step()) {
          val _item: FlashCard
          val _tmpUid: Int
          _tmpUid = _stmt.getLong(_columnIndexOfUid).toInt()
          val _tmpEnglishCard: String?
          if (_stmt.isNull(_columnIndexOfEnglishCard)) {
            _tmpEnglishCard = null
          } else {
            _tmpEnglishCard = _stmt.getText(_columnIndexOfEnglishCard)
          }
          val _tmpVietnameseCard: String?
          if (_stmt.isNull(_columnIndexOfVietnameseCard)) {
            _tmpVietnameseCard = null
          } else {
            _tmpVietnameseCard = _stmt.getText(_columnIndexOfVietnameseCard)
          }
          val _tmpAudioCard: String?
          if (_stmt.isNull(_columnIndexOfAudioCard)) {
            _tmpAudioCard = null
          } else {
            _tmpAudioCard = _stmt.getText(_columnIndexOfAudioCard)
          }
          _item = FlashCard(_tmpUid,_tmpEnglishCard,_tmpVietnameseCard,_tmpAudioCard)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteByCardPair(english: String, vietnamese: String) {
    val _sql: String = "DELETE FROM FlashCards WHERE english_card = ? AND vietnamese_card = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, english)
        _argIndex = 2
        _stmt.bindText(_argIndex, vietnamese)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateFlashCardByPair(
    englishOld: String,
    vietnameseOld: String,
    englishNew: String,
    vietnameseNew: String,
    audioNew: String?,
  ) {
    val _sql: String = "UPDATE FlashCards SET english_card = ?, vietnamese_card = ?, audio_card = ? WHERE english_card = ? AND vietnamese_card = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, englishNew)
        _argIndex = 2
        _stmt.bindText(_argIndex, vietnameseNew)
        _argIndex = 3
        if (audioNew == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, audioNew)
        }
        _argIndex = 4
        _stmt.bindText(_argIndex, englishOld)
        _argIndex = 5
        _stmt.bindText(_argIndex, vietnameseOld)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
