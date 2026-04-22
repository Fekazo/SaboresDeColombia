package com.previo.p2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.previo.p2.ui.screen.DetailScreen
import com.previo.p2.ui.screen.FavoritesScreen
import com.previo.p2.ui.screen.HomeScreen
import com.previo.p2.ui.screen.RegionScreen
import com.previo.p2.ui.screen.SearchScreen
import com.previo.p2.ui.screen.SplashScreen

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Splash.route,
        modifier = modifier
    ) {
        composable(NavRoutes.Splash.route) {
            SplashScreen(onSplashFinished = {
                navController.navigate(NavRoutes.Home.route) {
                    popUpTo(NavRoutes.Splash.route) { inclusive = true }
                }
            })
        }
        composable(NavRoutes.Home.route) {
            HomeScreen(
                onMealClick = { mealId ->
                    navController.navigate(NavRoutes.Detail.createRoute(mealId))
                },
                onRegionClick = { area, displayName ->
                    navController.navigate(NavRoutes.Region.createRoute(area, displayName))
                }
            )
        }
        composable(NavRoutes.Search.route) {
            SearchScreen(onMealClick = { mealId ->
                navController.navigate(NavRoutes.Detail.createRoute(mealId))
            })
        }
        composable(NavRoutes.Favorites.route) {
            FavoritesScreen(onMealClick = { mealId ->
                navController.navigate(NavRoutes.Detail.createRoute(mealId))
            })
        }
        composable(
            route = NavRoutes.Region.route,
            arguments = listOf(
                navArgument("area") { type = NavType.StringType },
                navArgument("displayName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val area = backStackEntry.arguments?.getString("area") ?: ""
            val displayName = backStackEntry.arguments?.getString("displayName") ?: ""
            RegionScreen(
                area = area,
                displayName = displayName,
                onMealClick = { mealId ->
                    navController.navigate(NavRoutes.Detail.createRoute(mealId))
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = NavRoutes.Detail.route,
            arguments = listOf(navArgument("mealId") { type = NavType.StringType })
        ) { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId") ?: ""
            DetailScreen(
                mealId = mealId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}