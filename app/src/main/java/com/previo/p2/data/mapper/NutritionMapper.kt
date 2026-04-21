package com.previo.p2.data.mapper

import com.previo.p2.data.remote.dto.EdamamResponseDto
import com.previo.p2.domain.model.Nutrition

fun EdamamResponseDto.toDomain(): Nutrition? {
    val hit = hits?.firstOrNull() ?: return null
    val nutrients = hit.recipe.totalNutrients
    return Nutrition(
        calories = nutrients.calories?.quantity ?: 0.0,
        protein = nutrients.protein?.quantity ?: 0.0,
        fat = nutrients.fat?.quantity ?: 0.0,
        carbs = nutrients.carbs?.quantity ?: 0.0,
        label = hit.recipe.label
    )
}