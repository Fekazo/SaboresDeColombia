package com.previo.p2.domain.model

data class MealSummary(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
    val translatedName: String? = null
)