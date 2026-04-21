package com.previo.p2.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EdamamResponseDto(
    @Json(name = "hits") val hits: List<EdamamHitDto>?
)

@JsonClass(generateAdapter = true)
data class EdamamHitDto(
    @Json(name = "recipe") val recipe: EdamamRecipeDto
)

@JsonClass(generateAdapter = true)
data class EdamamRecipeDto(
    @Json(name = "label") val label: String,
    @Json(name = "totalNutrients") val totalNutrients: EdamamNutrientsDto
)

@JsonClass(generateAdapter = true)
data class EdamamNutrientsDto(
    @Json(name = "ENERC_KCAL") val calories: EdamamNutrientValueDto?,
    @Json(name = "PROCNT") val protein: EdamamNutrientValueDto?,
    @Json(name = "FAT") val fat: EdamamNutrientValueDto?,
    @Json(name = "CHOCDF") val carbs: EdamamNutrientValueDto?
)

@JsonClass(generateAdapter = true)
data class EdamamNutrientValueDto(
    @Json(name = "quantity") val quantity: Double,
    @Json(name = "unit") val unit: String
)