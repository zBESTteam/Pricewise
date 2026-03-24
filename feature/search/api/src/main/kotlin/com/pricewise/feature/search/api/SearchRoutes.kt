package com.pricewise.feature.search.api

object SearchRoutes {
    const val searchBaseRoute: String = "search"
    const val searchQueryArgument: String = "query"
    const val searchRoute: String =
        "$searchBaseRoute?$searchQueryArgument={$searchQueryArgument}"
}
