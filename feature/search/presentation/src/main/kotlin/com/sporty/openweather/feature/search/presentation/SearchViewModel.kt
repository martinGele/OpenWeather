package com.sporty.openweather.feature.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sporty.openweather.feature.search.domain.usecase.SearchPlacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchPlaces: SearchPlacesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val queries = MutableStateFlow("")

    init {
        queries
            .debounce(DEBOUNCE)
            .map(String::trim)
            .distinctUntilChanged()
            .onEach { query ->
                if (query.length < MIN_QUERY_LENGTH) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            results = emptyList(),
                            error = null
                        )
                    }
                }
            }
            .filter { it.length >= MIN_QUERY_LENGTH }
            .flatMapLatest { query ->
                flow {
                    _state.update { it.copy(isLoading = true, error = null) }
                    emit(searchPlaces(query))
                }.catch { e ->
                    _state.update {
                        it.copy(isLoading = false, error = e.message ?: "Something went wrong")
                    }
                }
            }
            .onEach { results ->
                _state.update { it.copy(isLoading = false, results = results) }
            }
            .launchIn(viewModelScope)
    }

    fun onIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.QueryChanged -> queries.value = intent.query
        }
    }

    private companion object {
        val DEBOUNCE = 300.milliseconds
        const val MIN_QUERY_LENGTH = 2
    }
}
