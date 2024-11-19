package com.product.hafzlktakip.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.product.hafzlktakip.model.entity.NoteEntity
import com.product.hafzlktakip.ui.viewmodels.SurahViewModel

@Composable
fun NotesScreen(viewModel: SurahViewModel = hiltViewModel()) {
    // Collecting notes from the ViewModel
    val notes by viewModel.notes.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var noteText by remember { mutableStateOf("") }
    var selectedNote by remember { mutableStateOf<NoteEntity?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var noteToDelete by remember { mutableStateOf<NoteEntity?>(null) }

    // State for search query
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedNote = null
                    noteText = ""
                    showDialog = true
                },
                modifier = Modifier.padding(bottom = 54.dp) // Padding at the bottom
            ) {
                Text("+")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Scaffold padding
                .padding(16.dp)
        ) {
            // Search TextField
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Ara") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Filter notes based on search query
            val filteredNotes = notes.filter { it.text.contains(searchQuery, ignoreCase = true) }

            // Listing notes
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(filteredNotes) { note ->
                    NoteItem(
                        note = note,
                        onEdit = {
                            selectedNote = note
                            noteText = note.text
                            showDialog = true
                        },
                        onDelete = {
                            noteToDelete = note
                            showDeleteDialog = true // Open delete confirmation dialog
                        }
                    )
                }
            }
        }
    }

    // Dialog for adding/editing notes
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = {
                    if (selectedNote == null) {
                        // Add new note
                        viewModel.addNote(noteText)
                    } else {
                        // Update existing note
                        selectedNote?.let { viewModel.updateNote(it.copy(text = noteText)) }
                    }
                    showDialog = false
                }) {
                    Text("Kaydet")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("İptal")
                }
            },
            title = { Text(text = if (selectedNote == null) "Yeni Not" else "Notu Düzenle") },
            text = {
                Column {
                    TextField(
                        value = noteText,
                        onValueChange = { noteText = it },
                        label = { Text("Not") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        )
    }

    // Dialog for deleting a note
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            confirmButton = {
                Button(onClick = {
                    noteToDelete?.let { viewModel.deleteNote(it) }
                    showDeleteDialog = false
                }) {
                    Text("Sil")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("İptal")
                }
            },
            title = { Text(text = "Notu Sil") },
            text = { Text(text = "Bu notu silmek istediğinizden emin misiniz?") }
        )
    }
}

@Composable
fun NoteItem(
    note: NoteEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            // Show shortened or full text
            Text(
                text = if (isExpanded) note.text else note.text.take(20) + if (note.text.length > 20) "..." else "",
                style = MaterialTheme.typography.bodyLarge,
            )
            if (note.text.length > 20) {
                Text(
                    text = if (isExpanded) "Daha az oku" else "Daha fazlasını oku",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { isExpanded = !isExpanded }
                )
            }
        }
        IconButton(onClick = onEdit) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "Düzenle")
        }
        IconButton(onClick = onDelete) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Sil")
        }
    }
}


