package com.pricewise.core.network.dto

import com.squareup.moshi.Json

data class NotificationDto(
    val id: Long,
    val type: String?,
    val source: String?,
    @param:Json(name = "external_id")
    val externalId: String?,
    val title: String?,
    @param:Json(name = "thumbnail_url")
    val thumbnailUrl: String?,
    @param:Json(name = "product_url")
    val productUrl: String?,
    @param:Json(name = "old_price")
    val oldPrice: Long?,
    @param:Json(name = "new_price")
    val newPrice: Long?,
    @param:Json(name = "created_at")
    val createdAt: String?,
)

data class NotificationListDto(
    val items: List<NotificationDto>?,
)

data class NotificationAckRequestDto(
    val ids: List<Long>,
)

data class NotificationAckResponseDto(
    val acked: Int?,
)
