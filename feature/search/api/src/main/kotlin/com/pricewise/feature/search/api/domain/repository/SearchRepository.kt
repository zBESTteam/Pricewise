package com.pricewise.feature.search.api.domain.repository

import com.pricewise.feature.search.api.domain.model.SearchResult

interface SearchRepository {
    suspend fun search(
        query: String,
        limit: Int,
        offset: Int,
        perSource: Boolean,
        partial: Boolean,
    ): SearchResult
}
