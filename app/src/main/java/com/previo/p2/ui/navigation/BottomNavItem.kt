package com.previo.p2.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object Home : BottomNavItem(NavRoutes.Home.route, "Inicio", Icons.Default.Home)
    data object Search : BottomNavItem(NavRoutes.Search.route, "Buscar", Icons.Default.Search)
    data object Favorites : BottomNavItem(NavRoutes.Favorites.route, "Favoritos", Icons.Default.Favorite)

    companion object {
        val items = listOf(Home, Search, Favorites)
    }
}