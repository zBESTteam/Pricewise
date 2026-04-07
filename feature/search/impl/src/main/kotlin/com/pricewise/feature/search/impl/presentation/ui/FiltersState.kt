package com.pricewise.feature.search.impl.presentation.ui

data class FiltersState(
    var isProductChosen: Boolean = false,
    var deliveryChosen: Delivery = Delivery.NONE,
    var onlyOriginals: Boolean = false,
    var onlyNew: Boolean = false,
    var onlyUsed: Boolean = false,
    var onlyMarketplaces: Boolean = false,
    var onlyOfflineShops: Boolean = false,
    var priceFrom: Long,
    var priceTo: Long,
    var popularDiapasonChosen: Int = 0,
    var canPayLater: Boolean = false
)

enum class Delivery(val value: Int, val text: String) {
    NONE(0, "Любая"),
    TODAY(1, "Сегодня"),
    TODAY_OR_TOMORROW(2, "Сегодня или завтра"),
    WEEK(3, "До 7 дней"),
    TWO_WEEKS(4, "До 14 дней"),
}