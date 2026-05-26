package com.lab.saboresdecolombia.core.domain.model

data class Recipe(
    val id: String,
    val name: String,
    val category: String,
    val area: String,
    val instructions: String,
    val thumbnail: String,
    val tags: String,
    val youtubeUrl: String,
    val ingredients: List<Ingredient>,
    val isFavorite: Boolean = false
)

data class Ingredient(
    val name: String,
    val measure: String
)
