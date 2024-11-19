package com.product.hafzlktakip.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ayahs")
data class AyahEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val number: Int,
    val text: String,
    val surahId: Int,
    val isMemorized: Boolean = false
)
