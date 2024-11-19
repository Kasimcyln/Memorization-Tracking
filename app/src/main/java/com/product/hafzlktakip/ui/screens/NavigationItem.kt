package com.product.hafzlktakip.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(var route: String, var icon: ImageVector, var title: String) {
    object SurahList : NavigationItem("surah_list", Icons.Default.List, "Sureler")
    object CompletedSurahs : NavigationItem("completed_surahs", Icons.Default.Check, "Tamamlananlar")
}
