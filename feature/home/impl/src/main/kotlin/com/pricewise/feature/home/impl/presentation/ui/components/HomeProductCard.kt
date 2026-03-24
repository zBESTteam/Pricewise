package com.pricewise.feature.home.impl.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pricewise.feature.home.impl.presentation.ui.ProductUiModel
import com.pricewise.core.ui.components.PriceWiseProductCard
import com.pricewise.core.ui.components.PriceWiseProductCardModel

@Composable
internal fun ProductCard(
    product: ProductUiModel,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier,
) {
    PriceWiseProductCard(
        product = PriceWiseProductCardModel(
            id = product.id,
            title = product.title,
            price = product.price,
            isFavorite = product.isFavorite,
            thumbnailUrl = product.thumbnailUrl,
            marketplaceName = product.marketplace.name,
            marketplaceShortName = product.marketplace.shortName,
            marketplaceLogoUrl = product.marketplace.logoUrl,
        ),
        onClick = onClick,
        onFavoriteClick = onFavoriteClick,
        modifier = modifier,
    )
}
