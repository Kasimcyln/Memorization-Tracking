package com.product.hafzlktakip.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.product.hafzlktakip.model.dao.AyahDao
import com.product.hafzlktakip.model.dao.NoteDao
import com.product.hafzlktakip.model.dao.SurahDao
import com.product.hafzlktakip.model.entity.AyahEntity
import com.product.hafzlktakip.model.entity.NoteEntity
import com.product.hafzlktakip.model.entity.SurahEntity

@Database(entities = [SurahEntity::class, AyahEntity::class, NoteEntity::class], version = 1)
abstract class QuranDatabase : RoomDatabase() {
    abstract fun surahDao(): SurahDao
    abstract fun ayahDao(): AyahDao
    abstract fun noteDao(): NoteDao
}
