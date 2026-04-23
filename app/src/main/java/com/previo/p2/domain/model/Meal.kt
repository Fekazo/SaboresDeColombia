package com.previo.p2.domain.model

data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strArea: String,
    val strMealThumb: String,
    val strInstructions: String,
    val ingredients: List<Ingredient>,
    val strYoutube: String? = null,
    val strTags: String? = null,
    val translatedName: String? = null,
    val translatedArea: String? = null,
    val translatedInstructions: String? = null
)

data class Ingredient(
    val name: String,
    val measure: String,
    val translatedName: String? = null
)