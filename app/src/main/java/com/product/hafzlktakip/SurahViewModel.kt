package com.product.hafzlktakip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurahViewModel @Inject constructor(
    private val repository: SurahRepository
) : ViewModel() {

    private val _surahs = MutableStateFlow<List<SurahEntity>>(emptyList())
    val surahs: StateFlow<List<SurahEntity>> = _surahs

    private val _ayahs = MutableStateFlow<List<AyahEntity>>(emptyList())
    val ayahs: StateFlow<List<AyahEntity>> = _ayahs

    private val _completedSurahs = MutableStateFlow<List<SurahEntity>>(emptyList())
    val completedSurahs: StateFlow<List<SurahEntity>> = _completedSurahs

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Dialog kontrolü için eklenen state'ler
    private val _showCompletionDialog = MutableStateFlow(false)
    val showCompletionDialog: StateFlow<Boolean> = _showCompletionDialog

    private val _completedSurahName = MutableStateFlow("")
    val completedSurahName: StateFlow<String> = _completedSurahName

    private val _notes = MutableStateFlow<List<NoteEntity>>(emptyList())
    val notes: StateFlow<List<NoteEntity>> = _notes


    init {
        loadAndSaveSurahs()
        loadNotes()
    }

    fun loadAndSaveSurahs() {
        viewModelScope.launch {
            _isLoading.value = true
            _surahs.value = repository.getAllSurahs()
            _completedSurahs.value = repository.getCompletedSurahs()
            _isLoading.value = false
        }
    }

    fun loadSurahs() {
        viewModelScope.launch {
            _surahs.value = repository.getAllSurahs()
        }
    }

    fun loadCompletedSurahs() {
        viewModelScope.launch {
            _completedSurahs.value = repository.getCompletedSurahs()
        }
    }

    fun loadAyahsForSurah(surahId: Int) {
        viewModelScope.launch {
            _ayahs.value = repository.getAyahsForSurah(surahId)
        }
    }

    fun updateAyahMemorization(ayahId: Int, isMemorized: Boolean) {
        viewModelScope.launch {
            val ayah = _ayahs.value.find { it.id == ayahId }
            if (ayah != null) {
                val updatedAyah = ayah.copy(isMemorized = isMemorized)
                repository.updateAyahMemorization(updatedAyah)

                // Akışı güncelle
                _ayahs.value = _ayahs.value.map { if (it.id == ayahId) updatedAyah else it }

                // Surenin tamamlanıp tamamlanmadığını kontrol et
                checkIfSurahCompleted(ayah.surahId)
            }
        }
    }

    private fun checkIfSurahCompleted(surahId: Int) {
        viewModelScope.launch {
            val ayahs = repository.getAyahsForSurah(surahId)
            if (ayahs.all { it.isMemorized }) {
                _completedSurahName.value = _surahs.value.find { it.id == surahId }?.name ?: ""
                _showCompletionDialog.value = true // Dialogu aç

                markSurahAsCompleted(surahId)
                loadSurahs()
                loadCompletedSurahs()
            }
        }
    }

    fun markSurahAsCompleted(surahId: Int) {
        viewModelScope.launch {
            repository.markSurahAsCompleted(surahId)
            repository.clearMemorizedAyahs(surahId)
            loadCompletedSurahs()
        }
    }

    fun moveSurahBack(surah: SurahEntity) {
        viewModelScope.launch {
            repository.moveSurahBack(surah)
            loadSurahs()
            loadCompletedSurahs()
        }
    }

    fun dismissCompletionDialog() {
        _showCompletionDialog.value = false
    }

    fun loadNotes() {
        viewModelScope.launch(Dispatchers.IO) { // IO dispatcher ile arka planda çalıştır
            val notesList = repository.getAllNotes() // Veritabanından notları al
            _notes.value = notesList // Alınan notları StateFlow ile güncelle
        }
    }


    fun addNote(text: String) {
        viewModelScope.launch {
            repository.insertNote(NoteEntity(text = text))
            loadNotes()
        }
    }

    fun updateNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.updateNote(note)
            loadNotes()
        }
    }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.deleteNote(note)
            loadNotes()
        }
    }
}
