package com.product.hafzlktakip.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.product.hafzlktakip.ui.viewmodels.SurahViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val viewModel: SurahViewModel = hiltViewModel() // ViewModel'i burada kullanmak için oluşturduk

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController, viewModel)
        }
    ) { innerPadding ->
        NavigationHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            viewModel = viewModel, // ViewModel'i NavigationHost'a geçiyoruz
            innerPadding = Modifier.padding(innerPadding) // PaddingValues olarak innerPadding'i geçiriyoruz
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, viewModel: SurahViewModel) {
    val items = listOf(
        BottomNavItem.SurahList,
        BottomNavItem.CompletedSurahs,
        BottomNavItem.Notes // Yeni sayfa

    )
    BottomNavigation(
        backgroundColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    // Geçiş yaparken ilgili ViewModel fonksiyonunu çağır
                    when (item.route) {
                        BottomNavItem.SurahList.route -> {
                            viewModel.loadSurahs() // Sureler listesi yenileniyor
                        }
                        BottomNavItem.CompletedSurahs.route -> {
                            viewModel.loadCompletedSurahs() // Tamamlananlar listesi yenileniyor
                        }
                    }

                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    object SurahList : BottomNavItem("surah_list", Icons.Default.List, "Sureler")
    object CompletedSurahs : BottomNavItem("completed_surahs", Icons.Default.Check, "Tamamlananlar")
    object Notes : BottomNavItem("notes", Icons.Default.Add, "Notlar") // Yeni sayfa eklendi
}
