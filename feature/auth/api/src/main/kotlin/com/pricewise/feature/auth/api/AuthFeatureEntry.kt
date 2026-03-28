package com.pricewise.feature.auth.api

import com.pricewise.navigation.api.NavigationFeatureEntry

interface AuthFeatureEntry : NavigationFeatureEntry {
    val loginRoute: String
    val registerRoute: String
}
