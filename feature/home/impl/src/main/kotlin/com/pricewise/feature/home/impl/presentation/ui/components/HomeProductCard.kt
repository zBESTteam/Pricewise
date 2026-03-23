package com.pricewise.feature.home.impl.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.pricewise.feature.home.impl.R
import com.pricewise.core.ui.R as CoreUiR
import com.pricewise.feature.home.impl.presentation.ui.HomeColors
import com.pricewise.feature.home.impl.presentation.ui.HomeDimens
import com.pricewise.feature.home.impl.presentation.ui.HomeShapes
import com.pricewise.feature.home.impl.presentation.ui.HomeTextStyles
import com.pricewise.feature.home.impl.presentation.ui.MarketplaceUiModel
import com.pricewise.feature.home.impl.presentation.ui.ProductUiModel
import com.pricewise.feature.home.impl.presentation.ui.RemoteImage

@Composable
internal fun ProductCard(
    product: ProductUiModel,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .clip(HomeShapes.ProductCard),
        shape = HomeShapes.ProductCard,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = HomeDimens.ZeroElevation),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = HomeDimens.ProductCardContentPadding,
                    vertical = HomeDimens.ProductCardContentPadding,
                ),
            horizontalArrangement = Arrangement.spacedBy(HomeDimens.ProductCardHorizontalSpacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProductThumbnail(
                product = product,
                modifier = Modifier
                    .width(HomeDimens.ProductThumbnailWidth)
                    .height(HomeDimens.ProductThumbnailHeight),
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(HomeDimens.ProductCardTextSpacing),
            ) {
                MarketplaceBadge(
                    product = product.marketplace,
                    modifier = Modifier,
                )
                Text(
                    text = product.title,
                    style = HomeTextStyles.ProductTitle,
                    color = HomeColors.PrimaryText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = product.price,
                    style = HomeTextStyles.ProductPrice,
                    color = HomeColors.PrimaryText,
                )
            }
            FavoriteButton(
                isFavorite = product.isFavorite,
                productTitle = product.title,
                onClick = onFavoriteClick,
                modifier = Modifier.align(Alignment.CenterVertically),
            )
        }
    }
}

@Composable
private fun ProductThumbnail(
    product: ProductUiModel,
    modifier: Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        if (product.thumbnailUrl == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = HomeColors.ThumbnailFallbackBackground,
                        shape = HomeShapes.ProductThumbnail,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Image,
                    contentDescription = null,
                    tint = HomeColors.SecondaryText,
                    modifier = Modifier.size(HomeDimens.PlaceholderIconSize),
                )
            }
        } else {
            RemoteImage(
                imageUrl = product.thumbnailUrl,
                contentDescription = product.title,
                modifier = Modifier.fillMaxSize(),
                shape = HomeShapes.ProductThumbnail,
                contentScale = ContentScale.Crop,
                requestSize = DpSize(
                    width = HomeDimens.ProductThumbnailWidth,
                    height = HomeDimens.ProductThumbnailHeight,
                ),
            )
        }
    }
}

@Composable
private fun MarketplaceBadge(
    product: MarketplaceUiModel,
    modifier: Modifier,
) {
    val marketplaceContentDescription = stringResource(
        R.string.home_marketplace_content_description,
        product.name,
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(HomeDimens.MarketplaceSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (product.logoUrl == null) {
            Text(
                text = product.shortName,
                style = HomeTextStyles.MarketplaceBadge,
                color = HomeColors.SecondaryText,
                modifier = Modifier.semantics {
                    contentDescription = marketplaceContentDescription
                },
            )
        } else {
            RemoteImage(
                imageUrl = product.logoUrl,
                contentDescription = marketplaceContentDescription,
                modifier = Modifier.size(HomeDimens.MarketplaceLogoSize),
                shape = null,
                contentScale = ContentScale.Fit,
                requestSize = DpSize(
                    width = HomeDimens.MarketplaceLogoSize,
                    height = HomeDimens.MarketplaceLogoSize,
                ),
            )
        }
        Text(
            text = product.name,
            style = HomeTextStyles.Marketplace,
            color = HomeColors.PrimaryText,
        )
    }
}

@Composable
private fun FavoriteButton(
    isFavorite: Boolean,
    productTitle: String,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val favoriteContentDescription = if (isFavorite) {
        stringResource(R.string.home_remove_favorite_description, productTitle)
    } else {
        stringResource(R.string.home_add_favorite_description, productTitle)
    }

    Box(
        modifier = modifier
            .requiredSize(HomeDimens.TouchTargetSize)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .semantics {
                role = Role.Button
                contentDescription = favoriteContentDescription
            },
        contentAlignment = Alignment.Center,
    ) {
        FavoriteHeartIcon(
            isFavorite = isFavorite,
            modifier = Modifier.size(
                width = HomeDimens.FavoriteIconWidth,
                height = HomeDimens.FavoriteIconHeight,
            ),
        )
    }
}

@Composable
private fun FavoriteHeartIcon(
    isFavorite: Boolean,
    modifier: Modifier,
) {
    Image(
        painter = painterResource(
            id = if (isFavorite) {
                CoreUiR.drawable.heart_clicked
            } else {
                CoreUiR.drawable.heart
            },
        ),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Fit,
    )
}
