package com.lab.saboresdecolombia.core.data.mapper

import com.google.gson.Gson
import com.lab.saboresdecolombia.core.data.local.entity.CachedRecipeEntity
import com.lab.saboresdecolombia.core.data.local.entity.RecipeFavoriteEntity
import com.lab.saboresdecolombia.core.data.remote.dto.MealDto
import com.lab.saboresdecolombia.core.domain.model.Ingredient
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RecipeMapperTest {

    private val sampleDto = MealDto(
        idMeal = "52772", strMeal = "Teriyaki Chicken", strCategory = "Chicken",
        strArea = "Japanese", strInstructions = "Mix soy sauce and mirin.",
        strMealThumb = "https://www.themealdb.com/images/media/meals/wvpsxx1468256321.jpg",
        strTags = "Meat", strYoutube = "https://www.youtube.com/watch?v=abc",
        strIngredient1 = "chicken", strMeasure1 = "2 cups",
        strIngredient2 = "soy sauce", strMeasure2 = "1 tbsp",
        strIngredient3 = null, strMeasure3 = null,
        strIngredient4 = null, strMeasure4 = null,
        strIngredient5 = null, strMeasure5 = null,
        strIngredient6 = null, strMeasure6 = null,
        strIngredient7 = null, strMeasure7 = null,
        strIngredient8 = null, strMeasure8 = null,
        strIngredient9 = null, strMeasure9 = null,
        strIngredient10 = null, strMeasure10 = null,
        strIngredient11 = null, strMeasure11 = null,
        strIngredient12 = null, strMeasure12 = null,
        strIngredient13 = null, strMeasure13 = null,
        strIngredient14 = null, strMeasure14 = null,
        strIngredient15 = null, strMeasure15 = null,
        strIngredient16 = null, strMeasure16 = null,
        strIngredient17 = null, strMeasure17 = null,
        strIngredient18 = null, strMeasure18 = null,
        strIngredient19 = null, strMeasure19 = null,
        strIngredient20 = null, strMeasure20 = null
    )

    @Test
    fun `MealDto toDomain maps all fields correctly`() {
        val recipe = with(RecipeMapper) { sampleDto.toDomain() }

        assertEquals("52772", recipe.id)
        assertEquals("Teriyaki Chicken", recipe.name)
        assertEquals("Chicken", recipe.category)
        assertEquals("Japanese", recipe.area)
        assertEquals("Meat", recipe.tags)
        assertEquals(2, recipe.ingredients.size)
        assertTrue(recipe.ingredients.any { it.measure.contains("2") })
        assertTrue(recipe.ingredients.any { it.name.contains("chicken") })
    }

    @Test
    fun `Domain toEntity round trip preserves data`() {
        val recipe = with(RecipeMapper) { sampleDto.toDomain() }
        val entity = with(RecipeMapper) { recipe.toEntity() }

        assertEquals(recipe.id, entity.id)
        assertEquals(recipe.name, entity.name)
        assertTrue(entity.ingredients.contains("chicken"))
    }

    @Test
    fun `FavoriteEntity toDomain preserves all fields`() {
        val entity = RecipeFavoriteEntity(
            id = "test", name = "Test Recipe", category = "Test",
            area = "Colombian", thumbnail = "url",
            instructions = "Do something",
            ingredients = "1 cup flour\n2 tbsp sugar", tags = "test"
        )
        val recipe = with(RecipeMapper) { entity.toDomain(isFavorite = true) }

        assertEquals("test", recipe.id)
        assertEquals("Test Recipe", recipe.name)
        assertEquals("Colombian", recipe.area)
        assertEquals(2, recipe.ingredients.size)
        assertTrue(recipe.isFavorite)
    }

    @Test
    fun `CachedEntity toDomain preserves ingredients via JSON`() {
        val ingredients = listOf(Ingredient("chicken", "2 cups"), Ingredient("rice", "1 cup"))
        val json = Gson().toJson(ingredients)
        val entity = CachedRecipeEntity(
            id = "1", name = "Test", category = "Cat", area = "Area",
            instructions = "Mix", thumbnail = "url", tags = "tag",
            youtubeUrl = "yt", ingredientsJson = json
        )

        val recipe = with(RecipeMapper) { entity.toDomain() }
        assertEquals(2, recipe.ingredients.size)
        assertEquals("chicken", recipe.ingredients[0].name)
        assertEquals("2 cups", recipe.ingredients[0].measure)
    }
}
