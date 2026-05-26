package com.lab.saboresdecolombia.core.data.repository

import com.lab.saboresdecolombia.core.data.local.dao.CachedRecipeDao
import com.lab.saboresdecolombia.core.data.mapper.RecipeMapper.toCacheEntity
import com.lab.saboresdecolombia.core.data.mapper.RecipeMapper.toDomain
import com.lab.saboresdecolombia.core.data.remote.MealApiService
import com.lab.saboresdecolombia.core.domain.model.Recipe
import com.lab.saboresdecolombia.core.domain.repository.RecipeRepository
import com.lab.saboresdecolombia.core.domain.usecase.RecipeTranslator
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepositoryImpl @Inject constructor(
    private val api: MealApiService,
    private val translator: RecipeTranslator,
    private val cachedRecipeDao: CachedRecipeDao
) : RecipeRepository {

    override suspend fun getPopularRecipes(): Result<List<Recipe>> {
        return try {
            val recipes = cachedRecipeDao.getRandomRecipes(6)
            if (recipes.isNotEmpty()) {
                Result.success(recipes.map { it.toDomain() })
            } else {
                val meals = (1..4).mapNotNull {
                    api.randomMeal().meals?.firstOrNull()
                }
                val domain = meals.map { translator.translateRecipe(it.toDomain()) }
                cachedRecipeDao.insertAll(domain.map { it.toCacheEntity() })
                Result.success(domain)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRecipesByRegion(region: String): Result<List<Recipe>> {
        return try {
            val cached = cachedRecipeDao.getAllRecipes()
            if (cached.isNotEmpty()) {
                val filtered = cached
                    .map { it.toDomain() }
                    .filter { it.area.equals(region, ignoreCase = true) }
                if (filtered.isNotEmpty()) {
                    return Result.success(filtered)
                }
            }
            val response = api.filterByArea(region)
            val meals = response.meals ?: emptyList()
            val recipes = meals.map { translator.translateRecipe(it.toDomain()) }
            cachedRecipeDao.insertAll(recipes.map { it.toCacheEntity() })
            Result.success(recipes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchRecipes(query: String): Result<List<Recipe>> {
        return try {
            val cached = cachedRecipeDao.getAllRecipes()
            if (cached.isNotEmpty()) {
                val filtered = cached
                    .map { it.toDomain() }
                    .filter {
                        it.name.contains(query, ignoreCase = true) ||
                        it.category.contains(query, ignoreCase = true) ||
                        it.area.contains(query, ignoreCase = true)
                    }
                return Result.success(filtered)
            }
            val response = api.searchMeals(query)
            val meals = response.meals ?: emptyList()
            val recipes = meals.map { translator.translateRecipe(it.toDomain()) }
            cachedRecipeDao.insertAll(recipes.map { it.toCacheEntity() })
            Result.success(recipes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRecipeDetail(id: String): Result<Recipe> {
        return try {
            val cached = cachedRecipeDao.getRecipeById(id)
            if (cached != null) {
                return Result.success(cached.toDomain())
            }
            val response = api.lookupMeal(id)
            val meal = response.meals?.firstOrNull()
                ?: return Result.failure(Exception("Receta no encontrada"))
            val recipe = translator.translateRecipe(meal.toDomain())
            cachedRecipeDao.insertAll(listOf(recipe.toCacheEntity()))
            Result.success(recipe)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllRecipes(): Result<List<Recipe>> {
        return try {
            val cached = cachedRecipeDao.getAllRecipes()
            if (cached.isNotEmpty()) {
                Result.success(cached.map { it.toDomain() })
            } else {
                val letters = 'a'..'z'
                val allMeals = coroutineScope {
                    letters.map { letter ->
                        async {
                            api.filterByFirstLetter(letter.toString()).meals ?: emptyList()
                        }
                    }.flatMap { it.await() }
                }
                val recipes = allMeals.map { translator.translateRecipe(it.toDomain()) }.distinctBy { it.id }
                cachedRecipeDao.insertAll(recipes.map { it.toCacheEntity() })
                Result.success(recipes)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
