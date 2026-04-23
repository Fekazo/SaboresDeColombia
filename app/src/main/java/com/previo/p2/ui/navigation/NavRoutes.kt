package com.previo.p2.ui.navigation

sealed class NavRoutes(val route: String) {
    data object Splash : NavRoutes("splash")
    data object Home : NavRoutes("home")
    data object Popular : NavRoutes("popular")
    data object AllMeals : NavRoutes("all_meals")
    data object Search : NavRoutes("search")
    data object Favorites : NavRoutes("favorites")
    data object Region : NavRoutes("region/{area}/{displayName}") {
        fun createRoute(area: String, displayName: String) = "region/$area/$displayName"
    }
    data object Detail : NavRoutes("detail/{mealId}") {
        fun createRoute(mealId: String) = "detail/$mealId"
    }
}