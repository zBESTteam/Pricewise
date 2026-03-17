package com.pricewise.feature.home.impl.presentation.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.pricewise.feature.home.impl.R
import com.pricewise.feature.home.impl.presentation.viewmodel.MainScreenViewModel
import com.pricewise.feature.home.impl.ui.theme.PriceWiseComposeTheme

@Composable
fun MainScreen(
    contentPadding: PaddingValues,
    modifier: Modifier,
) {
    val viewModel: MainScreenViewModel = hiltViewModel()
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    MainContent(
        state = state,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onPhotoSearchClick = viewModel::onPhotoSearchClick,
        onQuickActionClick = viewModel::onQuickActionClick,
        onPopularQueryClick = viewModel::onPopularQueryClick,
        onProductFavoriteClick = viewModel::onProductFavoriteClick,
        contentPadding = contentPadding,
        modifier = modifier,
    )
}

@Composable
fun MainContent(
    state: MainScreenState,
    onSearchQueryChange: (String) -> Unit,
    onPhotoSearchClick: () -> Unit,
    onQuickActionClick: (String) -> Unit,
    onPopularQueryClick: (String) -> Unit,
    onProductFavoriteClick: (String) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier,
) {
    val shouldShowLoadingState = state.isLoading &&
        state.quickActions.isEmpty() &&
        state.popularQueries.isEmpty() &&
        state.products.isEmpty()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(HomeScreenTokens.SectionSpacing),
        contentPadding = PaddingValues(bottom = HomeScreenTokens.ListBottomPadding),
    ) {
        item {
            HeaderSection(
                state = state,
                quickActions = state.quickActions,
                onQueryChange = onSearchQueryChange,
                onPhotoSearchClick = onPhotoSearchClick,
                onQuickActionClick = onQuickActionClick,
                isLoading = shouldShowLoadingState,
                modifier = Modifier,
            )
        }
        if (shouldShowLoadingState) {
            item {
                LoadingFeedSection(
                    modifier = Modifier,
                )
            }
        }
        if (state.popularQueries.isNotEmpty()) {
            item {
                PopularQueriesSection(
                    title = stringResource(R.string.home_popular_queries_title),
                    queries = state.popularQueries,
                    onQueryClick = onPopularQueryClick,
                    modifier = Modifier,
                )
            }
        }
        if (state.products.isNotEmpty()) {
            item {
                RecommendationsSection(
                    title = stringResource(R.string.home_recommendations_title),
                    products = state.products,
                    onFavoriteClick = onProductFavoriteClick,
                    modifier = Modifier,
                )
            }
        }
    }
}

@Composable
fun HeaderSection(
    state: MainScreenState,
    quickActions: List<QuickActionUiModel>,
    onQueryChange: (String) -> Unit,
    onPhotoSearchClick: () -> Unit,
    onQuickActionClick: (String) -> Unit,
    isLoading: Boolean,
    modifier: Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(HomeScreenTokens.MediumSpacing),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(HomeScreenTokens.HeaderShape)
                .background(HomeScreenTokens.HeaderGradient),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
                    .padding(
                        start = HomeScreenTokens.ScreenHorizontalPadding,
                        end = HomeScreenTokens.ScreenHorizontalPadding,
                        top = HomeScreenTokens.LargeSpacing,
                        bottom = HomeScreenTokens.MediumSpacing,
                    ),
                verticalArrangement = Arrangement.spacedBy(HomeScreenTokens.SmallSpacing),
            ) {
                SearchBar(
                    query = state.searchQuery,
                    onQueryChange = onQueryChange,
                    onPhotoSearchClick = onPhotoSearchClick,
                    modifier = Modifier,
                )
            }
        }
        if (quickActions.isNotEmpty()) {
            QuickActionCarousel(
                actions = quickActions,
                onActionClick = onQuickActionClick,
                modifier = Modifier,
            )
        } else if (isLoading) {
            QuickActionCarouselSkeleton(
                modifier = Modifier,
            )
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onPhotoSearchClick: () -> Unit,
    modifier: Modifier,
) {
    val photoSearchContentDescription = stringResource(R.string.home_photo_search_content_description)

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = HomeScreenTokens.SearchFieldHeight),
        shape = HomeScreenTokens.CardShape,
        singleLine = true,
        textStyle = HomeScreenTokens.SearchTextStyle,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                tint = HomeScreenTokens.SecondaryText,
            )
        },
        trailingIcon = {
            IconButton(
                onClick = onPhotoSearchClick,
                modifier = Modifier.semantics {
                    role = Role.Button
                    contentDescription = photoSearchContentDescription
                },
            ) {
                Icon(
                    imageVector = Icons.Outlined.CameraAlt,
                    contentDescription = null,
                    tint = HomeScreenTokens.SecondaryText,
                )
            }
        },
        placeholder = {
            Text(
                text = stringResource(R.string.home_search_placeholder),
                style = HomeScreenTokens.SearchTextStyle,
                color = HomeScreenTokens.SecondaryText,
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = HomeScreenTokens.PrimaryText,
            unfocusedTextColor = HomeScreenTokens.PrimaryText,
            cursorColor = HomeScreenTokens.PrimaryText,
        ),
    )
}

@Composable
fun QuickActionCarousel(
    actions: List<QuickActionUiModel>,
    onActionClick: (String) -> Unit,
    modifier: Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(HomeScreenTokens.SmallSpacing),
        contentPadding = PaddingValues(horizontal = HomeScreenTokens.ScreenHorizontalPadding),
    ) {
        items(actions, key = { it.id }) { action ->
            QuickActionCard(
                action = action,
                onClick = { onActionClick(action.id) },
                modifier = Modifier,
            )
        }
    }
}

@Composable
private fun QuickActionCarouselSkeleton(
    modifier: Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(HomeScreenTokens.SmallSpacing),
        contentPadding = PaddingValues(horizontal = HomeScreenTokens.ScreenHorizontalPadding),
    ) {
        items(count = 3) { index ->
            Box(
                modifier = Modifier
                    .width(HomeScreenTokens.QuickActionWidth)
                    .height(HomeScreenTokens.QuickActionHeight)
                    .clip(HomeScreenTokens.QuickActionShape)
                    .shimmerLoading(shape = HomeScreenTokens.QuickActionShape, delayMillis = index * 120)
                    .background(HomeScreenTokens.PlaceholderBaseColor),
            )
        }
    }
}

@Composable
fun QuickActionCard(
    action: QuickActionUiModel,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    Card(
        modifier = modifier
            .width(HomeScreenTokens.QuickActionWidth)
            .height(HomeScreenTokens.QuickActionHeight)
            .border(
                width = 1.dp,
                color = HomeScreenTokens.QuickActionBorder,
                shape = HomeScreenTokens.QuickActionShape,
            )
            .clickable(onClick = onClick),
        shape = HomeScreenTokens.QuickActionShape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .clip(HomeScreenTokens.CardShape)
                .background(action.background),
        ) {
            if (action.imageUrl != null) {
                RemoteImage(
                    imageUrl = action.imageUrl,
                    contentDescription = action.title,
                    modifier = Modifier.fillMaxSize(),
                    shape = HomeScreenTokens.CardShape,
                    contentScale = ContentScale.Crop,
                )
            } else {
                PlaceholderBox(
                    modifier = Modifier.fillMaxSize(),
                    shape = HomeScreenTokens.CardShape,
                    delayMillis = 0,
                    useShimmer = false,
                )
            }
        }
    }
}

@Composable
fun PopularQueriesSection(
    title: String,
    queries: List<PopularQueryUiModel>,
    onQueryClick: (String) -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(HomeScreenTokens.MediumSpacing),
    ) {
        SectionTitle(
            title = title,
            modifier = Modifier.padding(horizontal = HomeScreenTokens.ScreenHorizontalPadding),
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(HomeScreenTokens.SmallSpacing),
            contentPadding = PaddingValues(horizontal = HomeScreenTokens.ScreenHorizontalPadding),
        ) {
            items(queries, key = { it.id }) { query ->
                QueryChip(
                    query = query,
                    onClick = { onQueryClick(query.id) },
                    modifier = Modifier,
                )
            }
        }
    }
}

@Composable
fun QueryChip(
    query: PopularQueryUiModel,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    Surface(
        modifier = modifier
            .clip(HomeScreenTokens.ChipShape)
            .clickable(onClick = onClick),
        color = HomeScreenTokens.ChipBackground,
        shape = HomeScreenTokens.ChipShape,
    ) {
        Text(
            text = query.title,
            style = HomeScreenTokens.ChipTextStyle,
            color = HomeScreenTokens.SecondaryText,
            modifier = Modifier.padding(
                horizontal = HomeScreenTokens.MediumSpacing,
                vertical = HomeScreenTokens.SmallSpacing + 2.dp,
            ),
        )
    }
}

@Composable
fun RecommendationsSection(
    title: String,
    products: List<ProductUiModel>,
    onFavoriteClick: (String) -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(HomeScreenTokens.MediumSpacing),
    ) {
        SectionTitle(
            title = title,
            modifier = Modifier.padding(horizontal = HomeScreenTokens.ScreenHorizontalPadding),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(HomeScreenTokens.MediumSpacing),
            modifier = Modifier.padding(horizontal = HomeScreenTokens.ScreenHorizontalPadding),
        ) {
            products.forEach { product ->
                ProductCard(
                    product = product,
                    onFavoriteClick = { onFavoriteClick(product.id) },
                    modifier = Modifier,
                )
            }
        }
    }
}

@Composable
private fun LoadingFeedSection(
    modifier: Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(HomeScreenTokens.SectionSpacing),
    ) {
        LoadingPopularQueriesSection(
            modifier = Modifier,
        )
        LoadingRecommendationsSection(
            modifier = Modifier,
        )
    }
}

@Composable
private fun LoadingPopularQueriesSection(
    modifier: Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(HomeScreenTokens.MediumSpacing),
    ) {
        PlaceholderBox(
            modifier = Modifier
                .padding(horizontal = HomeScreenTokens.ScreenHorizontalPadding)
                .width(152.dp)
                .height(28.dp),
            shape = RoundedCornerShape(12.dp),
            delayMillis = 0,
            useShimmer = true,
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(HomeScreenTokens.SmallSpacing),
            contentPadding = PaddingValues(horizontal = HomeScreenTokens.ScreenHorizontalPadding),
        ) {
            items(count = 4) { index ->
                PlaceholderBox(
                    modifier = Modifier
                        .width((84 + index * 10).dp)
                        .height(36.dp),
                    shape = HomeScreenTokens.ChipShape,
                    delayMillis = index * 90,
                    useShimmer = true,
                )
            }
        }
    }
}

@Composable
private fun LoadingRecommendationsSection(
    modifier: Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(HomeScreenTokens.MediumSpacing),
    ) {
        PlaceholderBox(
            modifier = Modifier
                .padding(horizontal = HomeScreenTokens.ScreenHorizontalPadding)
                .width(174.dp)
                .height(28.dp),
            shape = RoundedCornerShape(12.dp),
            delayMillis = 0,
            useShimmer = true,
        )
        Column(
            modifier = Modifier.padding(horizontal = HomeScreenTokens.ScreenHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(HomeScreenTokens.MediumSpacing),
        ) {
            repeat(5) { index ->
                LoadingProductCard(
                    modifier = Modifier,
                    delayMillis = index * 120,
                )
            }
        }
    }
}

@Composable
private fun LoadingProductCard(
    modifier: Modifier,
    delayMillis: Int,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = HomeScreenTokens.CardShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(HomeScreenTokens.MediumSpacing),
            horizontalArrangement = Arrangement.spacedBy(HomeScreenTokens.MediumSpacing),
            verticalAlignment = Alignment.Top,
        ) {
            PlaceholderBox(
                modifier = Modifier.size(HomeScreenTokens.ProductThumbnailSize),
                shape = HomeScreenTokens.CardShape,
                delayMillis = delayMillis,
                useShimmer = true,
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                PlaceholderBox(
                    modifier = Modifier
                        .width(118.dp)
                        .height(16.dp),
                    shape = RoundedCornerShape(8.dp),
                    delayMillis = delayMillis + 70,
                    useShimmer = true,
                )
                PlaceholderBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp),
                    shape = RoundedCornerShape(8.dp),
                    delayMillis = delayMillis + 140,
                    useShimmer = true,
                )
                PlaceholderBox(
                    modifier = Modifier
                        .width(140.dp)
                        .height(14.dp),
                    shape = RoundedCornerShape(8.dp),
                    delayMillis = delayMillis + 210,
                    useShimmer = true,
                )
                PlaceholderBox(
                    modifier = Modifier
                        .width(74.dp)
                        .height(18.dp),
                    shape = RoundedCornerShape(8.dp),
                    delayMillis = delayMillis + 280,
                    useShimmer = true,
                )
            }
            PlaceholderBox(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .size(24.dp),
                shape = RoundedCornerShape(12.dp),
                delayMillis = delayMillis + 120,
                useShimmer = true,
            )
        }
    }
}

@Composable
fun ProductCard(
    product: ProductUiModel,
    onFavoriteClick: () -> Unit,
    modifier: Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = HomeScreenTokens.CardShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(HomeScreenTokens.MediumSpacing),
            horizontalArrangement = Arrangement.spacedBy(HomeScreenTokens.MediumSpacing),
            verticalAlignment = Alignment.Top,
        ) {
            ProductThumbnail(
                product = product,
                modifier = Modifier.size(HomeScreenTokens.ProductThumbnailSize),
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MarketplaceBadge(
                    product = product.marketplace,
                    modifier = Modifier,
                )
                Text(
                    text = product.title,
                    style = HomeScreenTokens.ProductTitleTextStyle,
                    color = HomeScreenTokens.PrimaryText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = product.price,
                    style = HomeScreenTokens.ProductPriceTextStyle,
                    color = HomeScreenTokens.PrimaryText,
                )
            }
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .requiredSize(HomeScreenTokens.TouchTargetSize),
            ) {
                Icon(
                    imageVector = if (product.isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (product.isFavorite) {
                        stringResource(R.string.home_remove_favorite_description, product.title)
                    } else {
                        stringResource(R.string.home_add_favorite_description, product.title)
                    },
                    tint = HomeScreenTokens.IconTint,
                )
            }
        }
    }
}

@Composable
private fun ProductThumbnail(
    product: ProductUiModel,
    modifier: Modifier,
) {
    Box(
        modifier = modifier
            .clip(HomeScreenTokens.CardShape)
            .background(product.thumbnailBackground)
            .padding(8.dp),
    ) {
        if (product.thumbnailUrl != null) {
            RemoteImage(
                imageUrl = product.thumbnailUrl,
                contentDescription = product.title,
                modifier = Modifier.fillMaxSize(),
                shape = HomeScreenTokens.CardShape,
                contentScale = ContentScale.Crop,
            )
        } else {
            PlaceholderBox(
                modifier = Modifier.fillMaxSize(),
                shape = HomeScreenTokens.CardShape,
                delayMillis = 0,
                useShimmer = false,
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
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(product.badgeBrush)
                .semantics {
                    contentDescription = marketplaceContentDescription
                },
            contentAlignment = Alignment.Center,
        ) {
            if (product.logoUrl != null) {
                RemoteImage(
                    imageUrl = product.logoUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(4.dp),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Text(
                    text = product.shortName,
                    style = HomeScreenTokens.MarketplaceBadgeTextStyle,
                    color = Color.White,
                )
            }
        }
        Text(
            text = product.name,
            style = HomeScreenTokens.MarketplaceTextStyle,
            color = HomeScreenTokens.PrimaryText,
        )
    }
}

@Composable
private fun RemoteImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier,
    shape: RoundedCornerShape,
    contentScale: ContentScale,
) {
    val context = LocalContext.current
    val imageRequest = ImageRequest.Builder(context)
        .data(imageUrl)
        .listener(
            onError = { request, result ->
                Log.e(
                    HOME_IMAGE_LOG_TAG,
                    "Failed to load image: ${request.data}",
                    result.throwable,
                )
            },
        )
        .build()

    SubcomposeAsyncImage(
        model = imageRequest,
        contentDescription = contentDescription,
        modifier = modifier.clip(shape),
        contentScale = contentScale,
        loading = {
            PlaceholderBox(
                modifier = Modifier.matchParentSize(),
                shape = shape,
                delayMillis = 0,
                useShimmer = true,
            )
        },
        error = {
            PlaceholderBox(
                modifier = Modifier.matchParentSize(),
                shape = shape,
                delayMillis = 0,
                useShimmer = false,
            )
        },
    )
}

private const val HOME_IMAGE_LOG_TAG = "HomeRemoteImage"

@Composable
private fun PlaceholderBox(
    modifier: Modifier,
    shape: RoundedCornerShape,
    delayMillis: Int,
    useShimmer: Boolean,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(HomeScreenTokens.PlaceholderBaseColor)
            .then(
                if (useShimmer) {
                    Modifier.shimmerLoading(shape = shape, delayMillis = delayMillis)
                } else {
                    Modifier
                },
            ),
    )
}

private fun Modifier.shimmerLoading(
    shape: RoundedCornerShape,
    delayMillis: Int,
): Modifier = composed {
    val shimmerTransition = rememberInfiniteTransition(label = "placeholder_transition")
    val shimmerShift by shimmerTransition.animateFloat(
        initialValue = -320f,
        targetValue = 640f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = HomeScreenTokens.ShimmerDurationMillis,
                delayMillis = delayMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "placeholder_shift",
    )
    val shimmerBrush = Brush.linearGradient(
        colors = HomeScreenTokens.ShimmerColors,
        start = Offset(shimmerShift, 0f),
        end = Offset(shimmerShift + 240f, 240f),
    )

    background(brush = shimmerBrush, shape = shape)
}

@Composable
private fun SectionTitle(
    title: String,
    modifier: Modifier,
) {
    Text(
        text = title,
        modifier = modifier,
        style = HomeScreenTokens.SectionTitleTextStyle,
        color = HomeScreenTokens.Greyscale700,
    )
}

private object HomeScreenTokens {
    val ScreenBackground = Color(0xFFFEFEFE)
    val HeaderGradient = Brush.linearGradient(
        colors = listOf(Color(0xFFFFAB35), Color(0xFFFF2424)),
    )
    val QuickActionBorder = Color(0xFF2B2B2B)
    val Greyscale700 = Color(0xFF232323)
    val PrimaryText = Color(0xFF171717)
    val SecondaryText = Color(0xFF8D9094)
    val ChipBackground = Color(0xFFF8F8F8)
    val IconTint = Color(0xFF292929)
    val PlaceholderBaseColor = Color(0xFFF1F1F1)
    val PlaceholderHighlightColor = Color(0xFFFAFAFA)

    val HeaderShape = RoundedCornerShape(bottomStart = 26.dp, bottomEnd = 26.dp)
    val CardShape = RoundedCornerShape(14.dp)
    val QuickActionShape = RoundedCornerShape(16.dp)
    val ChipShape = RoundedCornerShape(14.dp)

    val ScreenHorizontalPadding = 16.dp
    val LargeSpacing = 24.dp
    val MediumSpacing = 16.dp
    val SmallSpacing = 8.dp
    val SectionSpacing = 20.dp
    val SearchFieldHeight = 48.dp
    val QuickActionWidth = 98.dp
    val QuickActionHeight = 98.dp
    val ProductThumbnailSize = 94.dp
    val ListBottomPadding = 20.dp
    val TouchTargetSize = 48.dp
    const val ShimmerDurationMillis = 1100
    val ShimmerColors = listOf(
        PlaceholderBaseColor,
        PlaceholderHighlightColor,
        PlaceholderBaseColor,
    )

    val SearchTextStyle
        @Composable get() = androidx.compose.material3.MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Medium,
        )
    val ChipTextStyle
        @Composable get() = androidx.compose.material3.MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.SemiBold,
        )
    val SectionTitleTextStyle
        @Composable get() = androidx.compose.material3.MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.Bold,
        )
    val ProductTitleTextStyle
        @Composable get() = androidx.compose.material3.MaterialTheme.typography.bodyMedium.copy(
            lineHeight = androidx.compose.ui.unit.TextUnit.Unspecified,
        )
    val ProductPriceTextStyle
        @Composable get() = androidx.compose.material3.MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
        )
    val MarketplaceTextStyle
        @Composable get() = androidx.compose.material3.MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.Bold,
        )
    val MarketplaceBadgeTextStyle
        @Composable get() = androidx.compose.material3.MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
        )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 375, heightDp = 812)
@Composable
private fun MainScreenPreview() {
    PriceWiseComposeTheme {
        MainContent(
            state = MainScreenState(
                searchQuery = "",
                isLoading = true,
                quickActions = emptyList(),
                popularQueries = emptyList(),
                products = emptyList(),
            ),
            onSearchQueryChange = {},
            onPhotoSearchClick = {},
            onQuickActionClick = {},
            onPopularQueryClick = {},
            onProductFavoriteClick = {},
            contentPadding = PaddingValues(),
            modifier = Modifier,
        )
    }
}
