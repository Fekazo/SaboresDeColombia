package com.previo.p2.domain.model

data class Nutrition(
    val calories: Double,
    val protein: Double,
    val fat: Double,
    val carbs: Double,
    val label: String = ""
)