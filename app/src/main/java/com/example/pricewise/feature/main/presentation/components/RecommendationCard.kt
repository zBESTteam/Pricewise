package com.example.pricewise.feature.main.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.pricewise.R
import com.example.pricewise.core.ui.rememberPricewiseImageLoader
import com.example.pricewise.feature.main.domain.model.Merchant
import com.example.pricewise.feature.main.domain.model.ProductRecommendation
import com.example.pricewise.feature.main.presentation.MainTypography
import com.example.pricewise.ui.theme.RecommendationCardBackground
import java.text.NumberFormat
import java.util.Locale

@Composable
fun RecommendationCard(
    recommendation: ProductRecommendation,
    onFavoriteClick: (ProductRecommendation) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        tonalElevation = 1.dp,
        shadowElevation = 0.dp,
        color = RecommendationCardBackground
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 10.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RecommendationImage(
                imageUrl = recommendation.thumbnailUrl,
                title = recommendation.title,
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(14.dp))
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(88.dp),
                verticalArrangement = Arrangement.Top
            ) {
                MerchantRow(merchant = recommendation.merchant)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = recommendation.title,
                    style = MainTypography.ProductTitle,
                    color = Color(0xFF000000),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = formatPrice(recommendation.price),
                    style = MainTypography.Price,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            FavoriteButton(
                isFavorite = recommendation.isFavorite,
                onClick = { onFavoriteClick(recommendation) }
            )
        }
    }
}

@Composable
private fun RecommendationImage(
    imageUrl: String,
    title: String,
    modifier: Modifier = Modifier,
) {
    val orangeStart = colorResource(id = R.color.orange_gradient_start)
    val orangeEnd = colorResource(id = R.color.orange_gradient_end)
    val gradient = remember(orangeStart, orangeEnd) {
        Brush.linearGradient(
            colors = listOf(orangeStart, orangeEnd)
        )
    }
    val imageLoader = rememberPricewiseImageLoader()

    if (imageUrl.isBlank()) {
        ImagePlaceholder(
            modifier = modifier,
            gradient = gradient,
            showSpinner = false,
        )
        return
    }

    SubcomposeAsyncImage(
        model = imageUrl,
        imageLoader = imageLoader,
        contentDescription = title,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        loading = { ImagePlaceholder(modifier = modifier, gradient = gradient, showSpinner = true) },
        error = { ImagePlaceholder(modifier = modifier, gradient = gradient, showSpinner = false) }
    )
}

@Composable
private fun MerchantRow(
    merchant: Merchant,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MerchantBadge(merchant = merchant)
        Text(
            text = merchant.name,
            style = MainTypography.MerchantName,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun MerchantBadge(
    merchant: Merchant,
    modifier: Modifier = Modifier,
) {
    val orangeStart = colorResource(id = R.color.orange_gradient_start)
    val accent = colorResource(id = R.color.accent_purple)
    val badgeGradient = remember(orangeStart, accent) {
        Brush.linearGradient(
            colors = listOf(orangeStart, accent)
        )
    }
    val placeholderInitial = stringResource(id = R.string.merchant_placeholder_initial)
    val imageLoader = rememberPricewiseImageLoader()

    if (merchant.logoUrl.isBlank()) {
        Box(
            modifier = modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(badgeGradient),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = merchant.name.firstOrNull()?.uppercase() ?: placeholderInitial,
                style = MainTypography.ProductTitle,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    } else {
        SubcomposeAsyncImage(
            model = merchant.logoUrl,
            imageLoader = imageLoader,
            contentDescription = merchant.name,
            modifier = modifier
                .size(16.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            loading = {
                Box(
                    modifier = modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(badgeGradient),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 1.5.dp,
                        modifier = Modifier.size(10.dp)
                    )
                }
            },
            error = {
                Box(
                    modifier = modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(badgeGradient)
                )
            }
        )
    }
}

@Composable
private fun ImagePlaceholder(
    modifier: Modifier,
    gradient: Brush,
    showSpinner: Boolean,
) {
    Box(
        modifier = modifier.background(gradient),
        contentAlignment = Alignment.Center
    ) {
        if (showSpinner) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp,
                modifier = Modifier.size(22.dp)
            )
        } else {
            Text(
                text = stringResource(id = R.string.image_placeholder),
                style = MainTypography.Price,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val orangeStart = colorResource(id = R.color.orange_gradient_start)
    val orangeEnd = colorResource(id = R.color.orange_gradient_end)
    val gradient = remember(orangeStart, orangeEnd) {
        Brush.linearGradient(colors = listOf(orangeStart, orangeEnd))
    }

    Box(
        modifier = modifier
            .size(32.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isFavorite) {
            GradientIcon(
                imageVector = Icons.Filled.Favorite,
                brush = gradient,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun GradientIcon(
    imageVector: ImageVector,
    brush: Brush,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        tint = Color.White,
        modifier = modifier.drawWithCache {
            onDrawWithContent {
                drawContent()
                drawRect(brush = brush, blendMode = BlendMode.SrcAtop)
            }
        }
    )
}

@Composable
private fun formatPrice(price: Long): String {
    val formatter = remember {
        NumberFormat.getInstance(Locale.forLanguageTag("ru-RU"))
    }
    val formatted = remember(price) { formatter.format(price) }
    return stringResource(id = R.string.price_currency_template, formatted)
}
