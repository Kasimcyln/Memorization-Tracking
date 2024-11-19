package com.product.hafzlktakip

data class QuranData(
    val surahs: List<Surah>
)

data class Surah(
    val name: String,
    val ayahs: List<Ayah>
)

data class Ayah(
    val number: Int,
    val text: String
)
