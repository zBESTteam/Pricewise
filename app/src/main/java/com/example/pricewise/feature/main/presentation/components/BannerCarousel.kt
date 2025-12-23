package com.example.pricewise.feature.main.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import com.example.pricewise.R
import com.example.pricewise.core.ui.rememberPricewiseImageLoader
import com.example.pricewise.feature.main.domain.model.PromoBanner
import com.example.pricewise.feature.main.presentation.MainDimens

@Composable
fun BannerCarousel(
    banners: List<PromoBanner>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(banners, key = { it.id }) { banner ->
            BannerCard(banner = banner)
        }
    }
}

@Composable
private fun BannerCard(
    banner: PromoBanner,
    modifier: Modifier = Modifier,
) {
    val frameSize = MainDimens.BannerFrameSize
    val imageSize = MainDimens.BannerImageSize
    val cornerRadius = MainDimens.BannerCornerRadius
    val contentDescription = stringResource(
        id = R.string.banner_content_description,
        banner.title
    )
    val orangeStart = colorResource(id = R.color.orange_gradient_start)
    val orangeEnd = colorResource(id = R.color.orange_gradient_end)
    val gradient = remember(orangeStart, orangeEnd) {
        Brush.linearGradient(
            colors = listOf(orangeStart, orangeEnd)
        )
    }
    val imageLoader = rememberPricewiseImageLoader()

    Surface(
        modifier = modifier.size(frameSize),
        shape = RoundedCornerShape(cornerRadius),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = colorResource(id = R.color.banner_border)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            BannerImage(
                imageUrl = banner.imageUrl,
                contentDescription = contentDescription,
                size = imageSize,
                cornerRadius = cornerRadius,
                gradient = gradient,
                imageLoader = imageLoader,
            )
        }
    }
}

@Composable
private fun BannerImage(
    imageUrl: String,
    contentDescription: String,
    size: androidx.compose.ui.unit.Dp,
    cornerRadius: Dp,
    gradient: Brush,
    imageLoader: ImageLoader,
) {
    if (imageUrl.isBlank()) {
        BannerPlaceholder(size = size, cornerRadius = cornerRadius, gradient = gradient)
        return
    }
    SubcomposeAsyncImage(
        model = imageUrl,
        imageLoader = imageLoader,
        contentDescription = contentDescription,
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(Color.Transparent),
        contentScale = ContentScale.Crop,
        loading = { BannerPlaceholder(size = size, cornerRadius = cornerRadius, gradient = gradient, showSpinner = true) },
        error = { BannerPlaceholder(size = size, cornerRadius = cornerRadius, gradient = gradient) }
    )
}

@Composable
private fun BannerPlaceholder(
    size: Dp,
    cornerRadius: Dp,
    gradient: Brush,
    showSpinner: Boolean = false,
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(gradient),
        contentAlignment = Alignment.Center
    )
    {
        if (showSpinner) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.dp,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
