package com.product.hafzlktakip.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.product.hafzlktakip.model.entity.AyahEntity

@Dao
interface AyahDao {
    @Insert
    suspend fun insertAyah(ayah: AyahEntity)

    @Query("SELECT * FROM ayahs WHERE surahId = :surahId")
    suspend fun getAyahsForSurah(surahId: Int): List<AyahEntity>

    @Update
    suspend fun updateAyah(ayah: AyahEntity)

    @Query("UPDATE ayahs SET isMemorized = 0 WHERE surahId = :surahId")
    suspend fun clearMemorizationForSurah(surahId: Int)
}
