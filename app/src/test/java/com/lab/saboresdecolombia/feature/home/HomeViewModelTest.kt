package com.lab.saboresdecolombia.feature.home

import com.lab.saboresdecolombia.core.domain.model.Ingredient
import com.lab.saboresdecolombia.core.domain.model.Recipe
import com.lab.saboresdecolombia.core.domain.repository.RecipeRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository: RecipeRepository = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadPopularRecipes updates state with recipes on success`() = runTest {
        val recipes = listOf(
            Recipe(
                id = "1", name = "Ajiaco", category = "Chicken",
                area = "Colombian", instructions = "Cook", thumbnail = "url",
                tags = "Soup", youtubeUrl = "", ingredients = listOf(Ingredient("chicken", "1"))
            ),
            Recipe(
                id = "2", name = "Bandeja Paisa", category = "Beef",
                area = "Colombian", instructions = "Fry", thumbnail = "url",
                tags = "Main", youtubeUrl = "", ingredients = listOf(Ingredient("beans", "2 cups"))
            )
        )
        coEvery { repository.getPopularRecipes() } returns Result.success(recipes)

        val viewModel = HomeViewModel(repository)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(2, state.popularRecipes.size)
        assertEquals("Ajiaco", state.popularRecipes[0].name)
        assertFalse(state.isLoading)
        assertEquals(null, state.error)
    }

    @Test
    fun `loadPopularRecipes updates state with error on failure`() = runTest {
        coEvery { repository.getPopularRecipes() } returns Result.failure(Exception("Network error"))

        val viewModel = HomeViewModel(repository)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(0, state.popularRecipes.size)
        assertEquals("Network error", state.error)
        assertFalse(state.isLoading)
    }

    @Test
    fun `state shows empty recipes immediately after creation`() = runTest {
        coEvery { repository.getPopularRecipes() } returns Result.success(emptyList())
        val viewModel = HomeViewModel(repository)
        val state = viewModel.uiState.value
        assertTrue(state.popularRecipes.isEmpty())
        // isLoading is false initially because coroutine hasn't started yet (test dispatcher)
    }
}
