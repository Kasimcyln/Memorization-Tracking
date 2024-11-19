package com.product.hafzlktakip

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SurahEntity::class, AyahEntity::class,NoteEntity::class], version = 1)
abstract class QuranDatabase : RoomDatabase() {
    abstract fun surahDao(): SurahDao
    abstract fun ayahDao(): AyahDao
    abstract fun noteDao(): NoteDao
}
