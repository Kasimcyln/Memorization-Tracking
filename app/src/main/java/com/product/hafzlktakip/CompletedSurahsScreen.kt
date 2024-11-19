package com.product.hafzlktakip

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CompletedSurahsScreen(viewModel: SurahViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.loadCompletedSurahs()
    }

    val completedSurahs = viewModel.completedSurahs.collectAsState().value
    var showDialog by remember { mutableStateOf(false) }
    var selectedSurah by remember { mutableStateOf<SurahEntity?>(null) }

    // Arama işlemi için gerekli state
    var searchText by remember { mutableStateOf("") }
    val filteredSurahs = if (searchText.isEmpty()) {
        completedSurahs
    } else {
        completedSurahs.filter { it.name.contains(searchText, ignoreCase = true) }
    }

    Column {
        // Arama çubuğu
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Tamamlanan Sure Ara") },
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
                CompletedSurahItem(
                    surah = surah,
                    onResetSurah = {
                        selectedSurah = surah
                        showDialog = true // Dialog'u aç
                    }
                )
            }
        }
    }

    // Dialog gösterimi
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = {
                    selectedSurah?.let { viewModel.moveSurahBack(it) }
                    showDialog = false
                }) {
                    Text("Tamam")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("İptal")
                }
            },
            title = { Text(text = "${selectedSurah?.name} geri taşınacak") },
            text = { Text(text = "${selectedSurah?.name} geri taşımak istediğinizden emin misiniz?") }
        )
    }
}

@Composable
fun CompletedSurahItem(
    surah: SurahEntity,
    onResetSurah: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = surah.name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "Geri Taşı",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable { onResetSurah() }
                .padding(8.dp)
        )
    }
}
