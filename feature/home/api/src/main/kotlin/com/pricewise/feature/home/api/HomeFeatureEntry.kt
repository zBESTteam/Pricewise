package com.pricewise.feature.home.api

import com.pricewise.navigation.api.NavigationFeatureEntry

interface HomeFeatureEntry : NavigationFeatureEntry {
    val homeRoute: String
}
