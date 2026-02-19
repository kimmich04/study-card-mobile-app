package com.example.annamstudyroomapp

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _flashCardDao: Lazy<FlashCardDao> = lazy {
    FlashCardDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1, "5c11078a83cc2daa2cfdb76d1f6a6c34", "c59f828470ca34e33a834f023340c8fc") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `FlashCards` (`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `english_card` TEXT, `vietnamese_card` TEXT, `audio_card` TEXT)")
        connection.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_FlashCards_english_card_vietnamese_card` ON `FlashCards` (`english_card`, `vietnamese_card`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '5c11078a83cc2daa2cfdb76d1f6a6c34')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `FlashCards`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsFlashCards: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsFlashCards.put("uid", TableInfo.Column("uid", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashCards.put("english_card", TableInfo.Column("english_card", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashCards.put("vietnamese_card", TableInfo.Column("vietnamese_card", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashCards.put("audio_card", TableInfo.Column("audio_card", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysFlashCards: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesFlashCards: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesFlashCards.add(TableInfo.Index("index_FlashCards_english_card_vietnamese_card", true, listOf("english_card", "vietnamese_card"), listOf("ASC", "ASC")))
        val _infoFlashCards: TableInfo = TableInfo("FlashCards", _columnsFlashCards, _foreignKeysFlashCards, _indicesFlashCards)
        val _existingFlashCards: TableInfo = read(connection, "FlashCards")
        if (!_infoFlashCards.equals(_existingFlashCards)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |FlashCards(com.example.annamstudyroomapp.FlashCard).
              | Expected:
              |""".trimMargin() + _infoFlashCards + """
              |
              | Found:
              |""".trimMargin() + _existingFlashCards)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "FlashCards")
  }

  public override fun clearAllTables() {
    super.performClear(false, "FlashCards")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(FlashCardDao::class, FlashCardDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun flashCardDao(): FlashCardDao = _flashCardDao.value
}
