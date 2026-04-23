package com.previo.p2.domain.model

import java.time.LocalDateTime

data class Favorite(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
    val strArea: String,
    val translatedName: String? = null,
    val savedAt: LocalDateTime = LocalDateTime.now()
)