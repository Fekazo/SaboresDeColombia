package com.lab.saboresdecolombia.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lab.saboresdecolombia.feature.allrecipes.AllRecipesScreen
import com.lab.saboresdecolombia.feature.detail.DetailScreen
import com.lab.saboresdecolombia.feature.favorites.FavoritesScreen
import com.lab.saboresdecolombia.feature.home.HomeScreen
import com.lab.saboresdecolombia.feature.regionlist.RegionListScreen
import com.lab.saboresdecolombia.feature.search.SearchScreen
import com.lab.saboresdecolombia.feature.splash.SplashScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.SPLASH
    ) {
        composable(NavRoutes.SPLASH) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.HOME) {
            HomeScreen(
                onRegionClick = { region ->
                    navController.navigate(NavRoutes.regionList(region))
                },
                onRecipeClick = { id ->
                    navController.navigate(NavRoutes.detail(id))
                },
                onFavoritesClick = {
                    navController.navigate(NavRoutes.FAVORITES)
                },
                onAllRecipesClick = {
                    navController.navigate(NavRoutes.ALL_RECIPES)
                }
            )
        }

        composable(
            route = NavRoutes.REGION_LIST,
            arguments = listOf(navArgument("region") { type = NavType.StringType })
        ) { backStackEntry ->
            val region = backStackEntry.arguments?.getString("region") ?: ""
            RegionListScreen(
                region = region,
                onRecipeClick = { id ->
                    navController.navigate(NavRoutes.detail(id))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            DetailScreen(
                recipeId = id,
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.SEARCH) {
            SearchScreen(
                onRecipeClick = { id ->
                    navController.navigate(NavRoutes.detail(id))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.FAVORITES) {
            FavoritesScreen(
                onRecipeClick = { id ->
                    navController.navigate(NavRoutes.detail(id))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.ALL_RECIPES) {
            AllRecipesScreen(
                onRecipeClick = { id ->
                    navController.navigate(NavRoutes.detail(id))
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
