package com.product.hafzlktakip

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import javax.inject.Inject

class SurahRepository @Inject constructor(
    private val surahDao: SurahDao,
    private val ayahDao: AyahDao,
    private val noteDao: NoteDao,
    private val context: Context

) {
    // Veritabanında veri olup olmadığını kontrol et
    private suspend fun isDatabasePopulated(): Boolean = withContext(Dispatchers.IO) {
        return@withContext surahDao.getUncompletedSurahs().isNotEmpty()
    }

    // Sureleri getir, eğer veritabanı boşsa JSON'dan verileri yükle
    suspend fun getAllSurahs(): List<SurahEntity> = withContext(Dispatchers.IO) {
        if (!isDatabasePopulated()) {
            loadSurahsFromAssets()
        }
        return@withContext surahDao.getUncompletedSurahs()
    }

    // Ayetleri getir
    suspend fun getAyahsForSurah(surahId: Int): List<AyahEntity> = withContext(Dispatchers.IO) {
        return@withContext ayahDao.getAyahsForSurah(surahId)
    }

    // Ayetin ezberleme durumunu güncelle
    suspend fun updateAyahMemorization(ayah: AyahEntity) = withContext(Dispatchers.IO) {
        ayahDao.updateAyah(ayah)
    }

    // Tamamlanmış sureleri getir
    suspend fun getCompletedSurahs(): List<SurahEntity> = withContext(Dispatchers.IO) {
        return@withContext surahDao.getCompletedSurahs()
    }

    // JSON'dan verileri yükle
    private suspend fun loadSurahsFromAssets() {
        withContext(Dispatchers.IO) {
            val inputStream = context.assets.open("quran_surahs.json")
            val reader = InputStreamReader(inputStream)
            val type = object : TypeToken<QuranData>() {}.type
            val quranData: QuranData = Gson().fromJson(reader, type)

            quranData.surahs.forEach { surah ->
                val surahEntity = SurahEntity(name = surah.name)
                val surahId = surahDao.insertSurah(surahEntity).toInt()

                surah.ayahs.forEach { ayah ->
                    val ayahEntity = AyahEntity(
                        number = ayah.number,
                        text = ayah.text,
                        surahId = surahId
                    )
                    ayahDao.insertAyah(ayahEntity)
                }
            }
        }
    }
    /*

        // Sureyi tamamlandı olarak işaretle
        suspend fun markSurahAsCompleted(surahId: Int) = withContext(Dispatchers.IO) {
            surahDao.markAsCompleted(surahId)
            ayahDao.clearMemorizationForSurah(surahId)
        }
    */

    // Sureyi tekrar aktif olarak işaretle
    suspend fun moveSurahBack(surah: SurahEntity) = withContext(Dispatchers.IO) {
        surahDao.markAsUncompleted(surah.id)
    }


    suspend fun markSurahAsCompleted(surahId: Int) = withContext(Dispatchers.IO) {
        val surah = surahDao.getSurahById(surahId)
        if (surah != null) {
            surahDao.updateSurah(surah.copy(isCompleted = true))
        }
    }

    suspend fun clearMemorizedAyahs(surahId: Int) = withContext(Dispatchers.IO) {
        val ayahs = ayahDao.getAyahsForSurah(surahId)
        ayahs.forEach { ayah ->
            ayahDao.updateAyah(ayah.copy(isMemorized = false))
        }
    }

    suspend fun getAllNotes(): List<NoteEntity> {
        return noteDao.getAllNotes() ?: emptyList()
    }

    suspend fun insertNote(note: NoteEntity) = noteDao.insert(note)

    suspend fun updateNote(note: NoteEntity) = noteDao.update(note)

    suspend fun deleteNote(note: NoteEntity) = noteDao.delete(note)

}
