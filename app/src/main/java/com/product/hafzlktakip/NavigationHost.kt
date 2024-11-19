package com.product.hafzlktakip

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier, viewModel: SurahViewModel, innerPadding: Modifier) {
    NavHost(navController = navController, startDestination = BottomNavItem.SurahList.route) {
        composable(BottomNavItem.SurahList.route) {
            SurahListScreen(viewModel = viewModel) // Burada innerPadding'i geçiriyoruz
        }
        composable(BottomNavItem.CompletedSurahs.route) {
            CompletedSurahsScreen()
        }
        composable(BottomNavItem.Notes.route) {
            NotesScreen(viewModel = viewModel) // Notlar sayfasını çağır
        }

    }
}
