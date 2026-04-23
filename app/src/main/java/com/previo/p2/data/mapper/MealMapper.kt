package com.previo.p2.data.mapper

import com.previo.p2.data.local.entity.MealCacheEntity
import com.previo.p2.data.local.entity.MealSummaryCacheEntity
import com.previo.p2.data.remote.dto.MealDetailDto
import com.previo.p2.data.remote.dto.MealSummaryDto
import com.previo.p2.domain.model.Ingredient
import com.previo.p2.domain.model.Meal
import com.previo.p2.domain.model.MealSummary

fun MealDetailDto.toDomain(): Meal {
    val ingredients = listOf(
        strIngredient1 to strMeasure1, strIngredient2 to strMeasure2,
        strIngredient3 to strMeasure3, strIngredient4 to strMeasure4,
        strIngredient5 to strMeasure5, strIngredient6 to strMeasure6,
        strIngredient7 to strMeasure7, strIngredient8 to strMeasure8,
        strIngredient9 to strMeasure9, strIngredient10 to strMeasure10,
        strIngredient11 to strMeasure11, strIngredient12 to strMeasure12,
        strIngredient13 to strMeasure13, strIngredient14 to strMeasure14,
        strIngredient15 to strMeasure15, strIngredient16 to strMeasure16,
        strIngredient17 to strMeasure17, strIngredient18 to strMeasure18,
        strIngredient19 to strMeasure19, strIngredient20 to strMeasure20
    ).filter { (name, _) -> !name.isNullOrBlank() }
        .map { (name, measure) -> Ingredient(name!!, measure.orEmpty().trim()) }

    return Meal(
        idMeal = idMeal,
        strMeal = strMeal,
        strArea = strArea,
        strMealThumb = strMealThumb,
        strInstructions = strInstructions,
        ingredients = ingredients,
        strYoutube = strYoutube,
        strTags = strTags
    )
}

fun MealSummaryDto.toDomain(): MealSummary = MealSummary(
    idMeal = idMeal,
    strMeal = strMeal,
    strMealThumb = strMealThumb
)

fun Meal.toCacheEntity(): MealCacheEntity {
    val ingredientsJson = ingredients.joinToString("|") {
        "${it.name}::${it.measure}::${it.translatedName.orEmpty()}"
    }
    return MealCacheEntity(
        idMeal = idMeal,
        strMeal = strMeal,
        strArea = strArea,
        strMealThumb = strMealThumb,
        strInstructions = strInstructions,
        ingredientsJson = ingredientsJson,
        strYoutube = strYoutube,
        strTags = strTags,
        translatedName = translatedName,
        translatedArea = translatedArea,
        translatedInstructions = translatedInstructions
    )
}

fun MealCacheEntity.toDomain(): Meal {
    val ingredients = ingredientsJson.split("|")
        .filter { it.isNotBlank() }
        .map {
            val parts = it.split("::")
            Ingredient(
                name = parts.getOrElse(0) { "" },
                measure = parts.getOrElse(1) { "" },
                translatedName = parts.getOrElse(2) { "" }.ifBlank { null }
            )
        }
    return Meal(
        idMeal = idMeal,
        strMeal = strMeal,
        strArea = strArea,
        strMealThumb = strMealThumb,
        strInstructions = strInstructions,
        ingredients = ingredients,
        strYoutube = strYoutube,
        strTags = strTags,
        translatedName = translatedName,
        translatedArea = translatedArea,
        translatedInstructions = translatedInstructions
    )
}

fun MealSummary.toCacheEntity(cacheKey: String): MealSummaryCacheEntity = MealSummaryCacheEntity(
    idMeal = idMeal,
    strMeal = strMeal,
    strMealThumb = strMealThumb,
    cacheKey = cacheKey,
    translatedName = translatedName
)

fun MealSummaryCacheEntity.toDomain(): MealSummary = MealSummary(
    idMeal = idMeal,
    strMeal = strMeal,
    strMealThumb = strMealThumb,
    translatedName = translatedName
)