package com.lab.saboresdecolombia.feature.search

import com.lab.saboresdecolombia.core.domain.model.Ingredient
import com.lab.saboresdecolombia.core.domain.model.Recipe
import com.lab.saboresdecolombia.core.domain.repository.RecipeRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

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
    fun `onQueryChange triggers search after debounce`() = runTest {
        val recipes = listOf(
            Recipe(
                id = "1", name = "Ají de Gallina", category = "Chicken",
                area = "Colombian", instructions = "Cook", thumbnail = "url",
                tags = "", youtubeUrl = "", ingredients = emptyList()
            )
        )
        coEvery { repository.searchRecipes("Ají") } returns Result.success(recipes)

        val viewModel = SearchViewModel(repository)

        viewModel.onQueryChange("A")
        viewModel.onQueryChange("Aj")
        viewModel.onQueryChange("Ají")

        advanceTimeBy(600)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.results.size)
        assertEquals("Ají de Gallina", state.results[0].name)
        assertTrue(state.hasSearched)
    }

    @Test
    fun `query shorter than 3 chars does not search`() = runTest {
        val viewModel = SearchViewModel(repository)
        viewModel.onQueryChange("ab")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.results.isEmpty())
        assertEquals("ab", state.query)
    }
}
