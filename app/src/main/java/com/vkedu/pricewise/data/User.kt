package com.vkedu.pricewise.data

data class User(
    val uid: String = "",
    val email: String? = null,
    val favorites: List<FavoriteItem> = emptyList()
)