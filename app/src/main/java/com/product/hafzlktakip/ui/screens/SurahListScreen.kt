package com.product.hafzlktakip.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.product.hafzlktakip.model.entity.AyahEntity
import com.product.hafzlktakip.model.entity.SurahEntity
import com.product.hafzlktakip.ui.viewmodels.SurahViewModel

@Composable
fun SurahListScreen(viewModel: SurahViewModel = hiltViewModel()) {
    val isLoading by viewModel.isLoading.collectAsState()
    val surahs by viewModel.surahs.collectAsState()
    var expandedSurahId by remember { mutableStateOf<Int?>(null) }
    val ayahs by viewModel.ayahs.collectAsState()

    // Arama işlemi için gereken state'ler
    var searchText by remember { mutableStateOf("") }
    val filteredSurahs = if (searchText.isEmpty()) {
        surahs
    } else {
        surahs.filter { it.name.contains(searchText, ignoreCase = true) }
    }

    // Dialog kontrolü için
    val showCompletionDialog by viewModel.showCompletionDialog.collectAsState()
    val completedSurahName by viewModel.completedSurahName.collectAsState()

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column {
            // Arama çubuğu
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Sure Ara") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(filteredSurahs) { surah ->
                    val isExpanded = expandedSurahId == surah.id
                    SurahItem(
                        surah = surah,
                        isExpanded = isExpanded,
                        onClick = {
                            expandedSurahId = if (isExpanded) null else surah.id
                            viewModel.loadAyahsForSurah(surah.id)
                        },
                        ayahs = if (isExpanded) ayahs else emptyList(),
                        onMemorizeAyah = { ayahId, isMemorized ->
                            viewModel.updateAyahMemorization(ayahId, isMemorized)
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }

    // Dialog gösterimi
    if (showCompletionDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissCompletionDialog() },
            confirmButton = {
                Button(onClick = { viewModel.dismissCompletionDialog() }) {
                    Text("Tamam")
                }
            },
            title = { Text(text = "$completedSurahName tamamlandı") },
            text = { Text(text = "$completedSurahName tamamlananlar sayfasına taşındı.") }
        )
    }
}


@Composable
fun SurahItem(
    surah: SurahEntity,
    isExpanded: Boolean,
    onClick: () -> Unit,
    ayahs: List<AyahEntity>,
    onMemorizeAyah: (Int, Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = surah.name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(8.dp)
        )

        if (isExpanded) {
            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                ayahs.forEach { ayah ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = ayah.isMemorized,
                            onCheckedChange = { isChecked ->
                                onMemorizeAyah(ayah.id, isChecked)
                            }
                        )
                        Text(
                            text = ayah.text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}