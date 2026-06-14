package com.sporty.openweather.feature.search.presentation

import com.sporty.openweather.feature.search.domain.model.Coordinates
import com.sporty.openweather.feature.search.domain.model.Place
import com.sporty.openweather.feature.search.domain.usecase.SearchPlacesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val searchPlaces = mockk<SearchPlacesUseCase>()

    private val london = Place("London", "GB", coordinates = Coordinates(51.5, -0.12))


    @Test
    fun `initial state is default`() {
        assertEquals(SearchState(), SearchViewModel(searchPlaces).state.value)
    }

    @Test
    fun `a query shorter than the minimum clears results without searching`() =
        runTest(mainDispatcherRule.testDispatcher.scheduler) {
            val viewModel = SearchViewModel(searchPlaces)

            viewModel.onIntent(SearchIntent.QueryChanged("L"))
            advanceUntilIdle()

            with(viewModel.state.value) {
                assertEquals(false, isLoading)
                assertEquals(emptyList<Place>(), results)
            }
            coVerify(exactly = 0) { searchPlaces(any()) }
        }

    @Test
    fun `a valid query populates results after the debounce`() =
        runTest(mainDispatcherRule.testDispatcher.scheduler) {
            coEvery { searchPlaces("London") } returns listOf(london)
            val viewModel = SearchViewModel(searchPlaces)

            viewModel.onIntent(SearchIntent.QueryChanged("London"))
            advanceUntilIdle()

            with(viewModel.state.value) {
                assertEquals(false, isLoading)
                assertEquals(listOf(london), results)
            }
            coVerify(exactly = 1) { searchPlaces("London") }
        }

    @Test
    fun `a repository failure surfaces an error`() =
        runTest(mainDispatcherRule.testDispatcher.scheduler) {
            coEvery { searchPlaces(any()) } throws IllegalStateException("Network error")
            val viewModel = SearchViewModel(searchPlaces)

            viewModel.onIntent(SearchIntent.QueryChanged("London"))
            advanceUntilIdle()

            with(viewModel.state.value) {
                assertEquals(false, isLoading)
                assertEquals("Network error", error)
            }
        }
}
