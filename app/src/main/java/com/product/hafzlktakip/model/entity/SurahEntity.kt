package com.product.hafzlktakip.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "surahs")
data class SurahEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val isCompleted: Boolean = false
)
