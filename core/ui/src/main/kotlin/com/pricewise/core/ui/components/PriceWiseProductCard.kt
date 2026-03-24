package com.pricewise.core.ui.components

import Typography.Inter
import androidx.compose.foundation.Image as FoundationImage
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pricewise.core.ui.R
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity

data class PriceWiseProductCardModel(
    val id: String,
    val title: String,
    val price: String,
    val isFavorite: Boolean,
    val thumbnailUrl: String?,
    val marketplaceName: String,
    val marketplaceShortName: String,
    val marketplaceLogoUrl: String?,
)

@Composable
fun PriceWiseProductCard(
    product: PriceWiseProductCardModel,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = ProductCardTokens.CardShape,
        colors = CardDefaults.cardColors(containerColor = ProductCardTokens.SurfaceColor),
        elevation = CardDefaults.cardElevation(defaultElevation = ProductCardTokens.ZeroElevation),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ProductCardTokens.SurfaceColor)
                .padding(
                    horizontal = ProductCardTokens.ContentPadding,
                    vertical = ProductCardTokens.ContentPadding,
                ),
            horizontalArrangement = Arrangement.spacedBy(ProductCardTokens.HorizontalSpacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProductThumbnail(
                product = product,
                modifier = Modifier
                    .width(ProductCardTokens.ThumbnailWidth)
                    .height(ProductCardTokens.ThumbnailHeight),
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(ProductCardTokens.TextSpacing),
            ) {
                MarketplaceBadge(
                    product = product,
                    modifier = Modifier,
                )
                Text(
                    text = product.title,
                    style = ProductCardTokens.TitleTextStyle,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = product.price,
                    style = ProductCardTokens.PriceTextStyle,
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
    product: PriceWiseProductCardModel,
    modifier: Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        if (product.thumbnailUrl.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = ProductCardTokens.ThumbnailPlaceholderColor,
                        shape = ProductCardTokens.CardShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Image,
                    contentDescription = null,
                    tint = ProductCardTokens.PlaceholderIconTint,
                    modifier = Modifier.size(ProductCardTokens.PlaceholderIconSize),
                )
            }
        } else {
            RemoteImage(
                imageUrl = product.thumbnailUrl,
                contentDescription = product.title,
                modifier = Modifier.fillMaxSize(),
                shape = ProductCardTokens.CardShape,
                contentScale = ContentScale.Crop,
                requestSize = DpSize(
                    width = ProductCardTokens.ThumbnailWidth,
                    height = ProductCardTokens.ThumbnailHeight,
                ),
            )
        }
    }
}

@Composable
private fun MarketplaceBadge(
    product: PriceWiseProductCardModel,
    modifier: Modifier,
) {
    val marketplaceContentDescription = stringResource(
        R.string.product_marketplace_content_description,
        product.marketplaceName,
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(ProductCardTokens.MarketplaceSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (product.marketplaceLogoUrl.isNullOrBlank()) {
            Text(
                text = product.marketplaceShortName,
                style = ProductCardTokens.MarketplaceShortNameTextStyle,
                modifier = Modifier.semantics {
                    contentDescription = marketplaceContentDescription
                },
            )
        } else {
            RemoteImage(
                imageUrl = product.marketplaceLogoUrl,
                contentDescription = marketplaceContentDescription,
                modifier = Modifier.size(ProductCardTokens.MarketplaceLogoSize),
                shape = null,
                contentScale = ContentScale.Fit,
                requestSize = DpSize(
                    width = ProductCardTokens.MarketplaceLogoSize,
                    height = ProductCardTokens.MarketplaceLogoSize,
                ),
            )
        }
        Text(
            text = product.marketplaceName,
            style = ProductCardTokens.MarketplaceNameTextStyle,
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
        stringResource(R.string.product_remove_favorite_description, productTitle)
    } else {
        stringResource(R.string.product_add_favorite_description, productTitle)
    }

    Box(
        modifier = modifier
            .requiredSize(ProductCardTokens.FavoriteTouchTargetSize)
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
        FoundationImage(
            painter = painterResource(
                id = if (isFavorite) {
                    R.drawable.heart_clicked
                } else {
                    R.drawable.heart
                },
            ),
            contentDescription = null,
            modifier = Modifier.size(ProductCardTokens.FavoriteIconSize),
            contentScale = ContentScale.Fit,
        )
    }
}

@Composable
private fun RemoteImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier,
    shape: androidx.compose.ui.graphics.Shape?,
    contentScale: ContentScale,
    requestSize: DpSize?,
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val requestWidthPx = remember(requestSize, density) {
        requestSize?.width?.let { width -> with(density) { width.roundToPx() } }
    }
    val requestHeightPx = remember(requestSize, density) {
        requestSize?.height?.let { height -> with(density) { height.roundToPx() } }
    }
    val isSvgImage = remember(imageUrl) {
        imageUrl.substringBefore('?').endsWith(".svg", ignoreCase = true)
    }
    val imageRequest = remember(context, imageUrl, requestWidthPx, requestHeightPx) {
        ImageRequest.Builder(context)
            .data(imageUrl)
            .apply {
                if (requestWidthPx != null && requestHeightPx != null) {
                    size(requestWidthPx, requestHeightPx)
                }
                if (isSvgImage) {
                    decoderFactory(SvgDecoder.Factory())
                }
            }
            .crossfade(false)
            .build()
    }
    val imageModifier = if (shape == null) {
        Modifier.fillMaxSize()
    } else {
        Modifier
            .fillMaxSize()
            .clip(shape)
    }

    Box(modifier = modifier) {
        AsyncImage(
            model = imageRequest,
            contentDescription = contentDescription,
            modifier = imageModifier,
            contentScale = contentScale,
        )
    }
}

private object ProductCardTokens {
    val CardShape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp)
    val SurfaceColor = Color.White
    val PrimaryTextColor = Color(0xFF000000)
    val SecondaryTextColor = Color(0xFF8D9094)
    val ThumbnailPlaceholderColor = Color(0xFFF5F5F5)
    val PlaceholderIconTint = SecondaryTextColor
    val ZeroElevation = 0.dp
    val ThumbnailWidth = 100.dp
    val ThumbnailHeight = 95.dp
    val PlaceholderIconSize = 24.dp
    val ContentPadding = 9.dp
    val HorizontalSpacing = 21.dp
    val TextSpacing = 8.dp
    val MarketplaceSpacing = 6.dp
    val MarketplaceLogoSize = 16.dp
    val FavoriteTouchTargetSize = 48.dp
    val FavoriteIconSize = DpSize(width = 19.dp, height = 17.dp)
    val TitleTextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        color = PrimaryTextColor,
    )
    val PriceTextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W700,
        fontSize = 14.sp,
        lineHeight = 21.sp,
        color = PrimaryTextColor,
    )
    val MarketplaceShortNameTextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W700,
        fontSize = 11.sp,
        lineHeight = 13.sp,
        color = SecondaryTextColor,
    )
    val MarketplaceNameTextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W600,
        fontSize = 14.sp,
        lineHeight = 21.sp,
        color = PrimaryTextColor,
    )
}
