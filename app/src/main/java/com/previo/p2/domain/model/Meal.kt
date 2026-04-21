package com.previo.p2.domain.model

data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strArea: String,
    val strMealThumb: String,
    val strInstructions: String,
    val ingredients: List<Ingredient>,
    val strYoutube: String? = null,
    val strTags: String? = null
)

data class Ingredient(
    val name: String,
    val measure: String
)