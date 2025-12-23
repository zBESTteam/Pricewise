package com.example.pricewise.feature.search.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pricewise.feature.search.data.ApiSearchRepository
import com.example.pricewise.feature.search.domain.repository.SearchRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: SearchRepository = SearchDependencies.defaultRepository(),
    private val defaultLimit: Int = 20,
    private val perSource: Boolean = true,
    private val partial: Boolean = true,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var pollJob: Job? = null
    private var pollAttempts = 0

    fun onQueryChange(query: String) {
        val trimmed = query.trim()
        _uiState.update { state ->
            when {
                trimmed.isEmpty() -> {
                    pollJob?.cancel()
                    state.copy(
                        query = query,
                        submittedQuery = "",
                        isLoading = false,
                        items = emptyList(),
                        hasMore = false,
                        checkedSources = 0,
                        totalSources = DEFAULT_TOTAL_SOURCES,
                        pendingSources = emptyList(),
                    )
                }
                state.submittedQuery != trimmed -> {
                    pollJob?.cancel()
                    state.copy(
                        query = query,
                        items = emptyList(),
                        hasMore = false,
                        checkedSources = 0,
                        pendingSources = emptyList(),
                    )
                }
                else -> state.copy(query = query)
            }
        }
    }

    fun submitSearch() {
        pollJob?.cancel()
        pollAttempts = 0
        val query = _uiState.value.query.trim()
        if (query.length < 2) {
            return
        }
        viewModelScope.launch {
            performSearch(query)
        }
    }

    private suspend fun performSearch(query: String) {
        _uiState.update { it.copy(isLoading = true, submittedQuery = query) }
        runCatching {
            repository.search(
                query = query,
                limit = defaultLimit,
                offset = 0,
                perSource = perSource,
                partial = partial,
            )
        }
            .onSuccess { result ->
                val shouldPoll = partial && result.pendingSources.isNotEmpty() && pollAttempts < MAX_POLL_ATTEMPTS
                _uiState.update { state ->
                    val checked = result.checkedSources ?: state.checkedSources
                    val resolvedTotal = when {
                        result.totalSources != null && result.totalSources > 0 -> {
                            maxOf(result.totalSources, checked)
                        }
                        checked > state.totalSources -> checked
                        else -> state.totalSources
                    }
                    state.copy(
                        isLoading = shouldPoll,
                        items = result.items,
                        hasMore = result.hasMore,
                        checkedSources = checked,
                        totalSources = resolvedTotal,
                        pendingSources = result.pendingSources,
                    )
                }
                Log.d(TAG, "Search '$query' -> ${result.items.size} items")
                if (shouldPoll) {
                    schedulePoll(query)
                }
            }
            .onFailure { err ->
                Log.e(TAG, "Failed to search '$query'", err)
                _uiState.update { it.copy(isLoading = false, pendingSources = emptyList()) }
            }
    }

    private fun schedulePoll(query: String) {
        pollJob?.cancel()
        pollJob = viewModelScope.launch {
            pollAttempts += 1
            delay(POLL_INTERVAL_MS)
            performSearch(query)
        }
    }
}

private object SearchDependencies {
    fun defaultRepository(): SearchRepository {
        return ApiSearchRepository()
    }
}

private const val TAG = "SearchVM"
private const val POLL_INTERVAL_MS = 1200L
private const val MAX_POLL_ATTEMPTS = 8
