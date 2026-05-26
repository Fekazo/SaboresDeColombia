package com.lab.saboresdecolombia.navigation

object NavRoutes {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val REGION_LIST = "region_list/{region}"
    const val DETAIL = "detail/{id}"
    const val SEARCH = "search"
    const val FAVORITES = "favorites"
    const val ALL_RECIPES = "all_recipes"

    fun regionList(region: String) = "region_list/$region"
    fun detail(id: String) = "detail/$id"
}
