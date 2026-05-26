package com.lab.saboresdecolombia.core.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lab.saboresdecolombia.core.data.local.entity.CachedRecipeEntity
import com.lab.saboresdecolombia.core.data.local.entity.RecipeFavoriteEntity
import com.lab.saboresdecolombia.core.data.remote.dto.MealDto
import com.lab.saboresdecolombia.core.domain.model.Ingredient
import com.lab.saboresdecolombia.core.domain.model.Recipe

object RecipeMapper {

    fun MealDto.toDomain(): Recipe {
        return Recipe(
            id = idMeal,
            name = strMeal ?: "",
            category = strCategory ?: "",
            area = strArea ?: "",
            instructions = strInstructions ?: "",
            thumbnail = strMealThumb ?: "",
            tags = strTags ?: "",
            youtubeUrl = strYoutube ?: "",
            ingredients = getIngredients().map { ingredientStr ->
                val parts = ingredientStr.trim().split(" ", limit = 2)
                Ingredient(
                    name = if (parts.size > 1) parts[1] else ingredientStr,
                    measure = parts.firstOrNull() ?: ""
                )
            }
        )
    }

    fun Recipe.toEntity(): RecipeFavoriteEntity {
        return RecipeFavoriteEntity(
            id = id,
            name = name,
            category = category,
            area = area,
            thumbnail = thumbnail,
            instructions = instructions,
            ingredients = ingredients.joinToString("\n") { "${it.measure} ${it.name}" },
            tags = tags
        )
    }

    fun RecipeFavoriteEntity.toDomain(isFavorite: Boolean = true): Recipe {
        val ingredientList = ingredients.split("\n").mapNotNull { line ->
            if (line.isBlank()) return@mapNotNull null
            val parts = line.trim().split(" ", limit = 2)
            Ingredient(
                name = if (parts.size > 1) parts[1] else line.trim(),
                measure = parts.firstOrNull() ?: ""
            )
        }
        return Recipe(
            id = id,
            name = name,
            category = category,
            area = area,
            instructions = instructions,
            thumbnail = thumbnail,
            tags = tags,
            youtubeUrl = "",
            ingredients = ingredientList,
            isFavorite = isFavorite
        )
    }

    fun Recipe.toCacheEntity(): CachedRecipeEntity {
        val ingredientsJson = Gson().toJson(ingredients)
        return CachedRecipeEntity(
            id = id,
            name = name,
            category = category,
            area = area,
            instructions = instructions,
            thumbnail = thumbnail,
            tags = tags,
            youtubeUrl = youtubeUrl,
            ingredientsJson = ingredientsJson
        )
    }

    fun CachedRecipeEntity.toDomain(): Recipe {
        val ingredientList: List<Ingredient> = try {
            val type = object : TypeToken<List<Ingredient>>() {}.type
            Gson().fromJson(ingredientsJson, type)
        } catch (e: Exception) {
            emptyList()
        }
        return Recipe(
            id = id,
            name = name,
            category = category,
            area = area,
            instructions = instructions,
            thumbnail = thumbnail,
            tags = tags,
            youtubeUrl = youtubeUrl,
            ingredients = ingredientList
        )
    }
}
