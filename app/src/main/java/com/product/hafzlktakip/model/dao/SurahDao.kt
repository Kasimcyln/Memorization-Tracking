package com.product.hafzlktakip.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.product.hafzlktakip.model.entity.SurahEntity

@Dao
interface SurahDao {
    @Insert
    suspend fun insertSurah(surah: SurahEntity): Long

    @Query("SELECT * FROM surahs WHERE isCompleted = 0")
    suspend fun getUncompletedSurahs(): List<SurahEntity>

    @Query("SELECT * FROM surahs WHERE isCompleted = 1")
    suspend fun getCompletedSurahs(): List<SurahEntity>

    @Query("SELECT * FROM surahs WHERE id = :surahId")
    suspend fun getSurahById(surahId: Int): SurahEntity?

    @Update
    suspend fun updateSurah(surah: SurahEntity)

    @Query("UPDATE surahs SET isCompleted = 0 WHERE id = :surahId")
    suspend fun markAsUncompleted(surahId: Int)
}
