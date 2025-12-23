package com.example.pricewise.feature.search.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.annotation.StringRes
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import com.example.pricewise.R
import com.example.pricewise.core.ui.rememberPricewiseImageLoader
import com.example.pricewise.feature.main.domain.model.ProductRecommendation
import com.example.pricewise.feature.main.presentation.MainDimens
import com.example.pricewise.feature.main.presentation.MainTypography
import com.example.pricewise.feature.main.presentation.components.PricewiseSearchBar
import com.example.pricewise.feature.main.presentation.components.RecommendationCard

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SearchScreenContent(
        modifier = modifier,
        query = state.query,
        onQueryChange = viewModel::onQueryChange,
        onSearch = viewModel::submitSearch,
        recommendations = state.items,
        checkedSources = state.checkedSources,
        totalSources = state.totalSources,
        isLoading = state.isLoading,
    )
}

@Composable
private fun SearchScreenContent(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    recommendations: List<ProductRecommendation>,
    checkedSources: Int,
    totalSources: Int,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    val horizontalPadding = MainDimens.ContentHorizontalPadding
    val verticalSpacing = MainDimens.SectionVerticalSpacing
    val shownIds = remember { mutableStateListOf<String>() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing)
    ) {
        item {
            SearchHeader(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                horizontalPadding = horizontalPadding
            )
        }
        item {
            SearchStatusBlock(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding),
                checkedSources = checkedSources,
                totalSources = totalSources,
                isLoading = isLoading,
            )
        }
        item {
            SearchFiltersRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = horizontalPadding)
            )
        }
        if (recommendations.isEmpty() && isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    GradientLoadingIndicator(modifier = Modifier.size(48.dp))
                }
            }
        }
        if (recommendations.isNotEmpty()) {
            itemsIndexed(recommendations, key = { _, item -> item.id }) { index, item ->
                val alreadyShown = shownIds.contains(item.id)
                var visible by remember(item.id) { mutableStateOf(alreadyShown) }
                LaunchedEffect(item.id, alreadyShown) {
                    if (!alreadyShown) {
                        val delayMs = (index.coerceAtMost(6) * 40).toLong()
                        kotlinx.coroutines.delay(delayMs)
                        visible = true
                        shownIds.add(item.id)
                    }
                }
                if (alreadyShown) {
                    RecommendationCard(
                        recommendation = item,
                        onFavoriteClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = horizontalPadding)
                    )
                } else {
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(animationSpec = tween(220, easing = LinearEasing)) +
                            slideInVertically(
                                initialOffsetY = { (it * 0.08f).toInt() },
                                animationSpec = tween(260, easing = LinearEasing)
                            ),
                    ) {
                        RecommendationCard(
                            recommendation = item,
                            onFavoriteClick = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = horizontalPadding)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GradientLoadingIndicator(modifier: Modifier = Modifier) {
    val orangeStart = colorResource(id = R.color.orange_gradient_start)
    val orangeEnd = colorResource(id = R.color.orange_gradient_end)
    val gradient = remember(orangeStart, orangeEnd) {
        Brush.sweepGradient(listOf(orangeStart, orangeEnd, orangeStart))
    }
    val infinite = rememberInfiniteTransition(label = "spinner")
    val rotation by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    Canvas(modifier = modifier) {
        val strokeWidth = size.minDimension * 0.14f
        rotate(rotation) {
            drawArc(
                brush = gradient,
                startAngle = 0f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
    }
}

@Composable
private fun SearchHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    horizontalPadding: androidx.compose.ui.unit.Dp,
) {
    val orangeStart = colorResource(id = R.color.orange_gradient_start)
    val orangeEnd = colorResource(id = R.color.orange_gradient_end)
    val gradient = remember(orangeStart, orangeEnd) {
        Brush.linearGradient(colors = listOf(orangeStart, orangeEnd))
    }
    val heroShape = RoundedCornerShape(bottomStart = 22.dp, bottomEnd = 22.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush = gradient, shape = heroShape),
        contentAlignment = Alignment.TopCenter
    ) {
        PricewiseSearchBar(
            value = query,
            onValueChange = onQueryChange,
            onClear = { onQueryChange("") },
            onSearch = onSearch,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = horizontalPadding,
                    end = horizontalPadding,
                    top = 32.dp,
                    bottom = MainDimens.SearchBottomInset
                )
        )
    }
}

@Composable
private fun SearchStatusBlock(
    checkedSources: Int,
    totalSources: Int,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    val safeTotal = totalSources.coerceAtLeast(1)
    val safeChecked = checkedSources.coerceAtLeast(0)
    val displayChecked = if (!isLoading && safeChecked < safeTotal) safeTotal else safeChecked
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = stringResource(id = R.string.search_status_title),
            style = MainTypography.SectionTitle,
            color = MaterialTheme.colorScheme.onBackground
        )
        Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text(
                text = stringResource(
                    id = R.string.search_status_progress,
                    displayChecked,
                    safeTotal
                ),
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    fontFamily = FontFamily(Font(R.font.inter_semibold)),
                    fontWeight = FontWeight.W600,
                    color = Color(0xFF727272),
                ),
            )
            GradientProgressBar(
                progress = displayChecked.toFloat() / safeTotal.toFloat(),
                isLoading = isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun GradientProgressBar(
    progress: Float,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    val orangeStart = colorResource(id = R.color.orange_gradient_start)
    val orangeEnd = colorResource(id = R.color.orange_gradient_end)
    val gradient = remember(orangeStart, orangeEnd) {
        Brush.linearGradient(colors = listOf(orangeStart, orangeEnd))
    }
    val target = progress.coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = target,
        animationSpec = tween(durationMillis = 420, easing = LinearEasing),
        label = "progress"
    )
    val infinite = rememberInfiniteTransition(label = "wiggle")
    val wiggle by infinite.animateFloat(
        initialValue = -0.02f,
        targetValue = 0.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wiggle"
    )
    val baseProgress = if (isLoading) maxOf(animatedProgress, 0.02f) else animatedProgress
    val displayProgress = (baseProgress + if (isLoading) wiggle else 0f).coerceIn(0f, 1f)
    Box(
        modifier = modifier
            .height(6.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF1F1F1))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(displayProgress)
                .clip(RoundedCornerShape(10.dp))
                .background(gradient)
        )
    }
}

private data class FilterChipItem(
    val id: String,
    @StringRes val labelResId: Int? = null,
    @StringRes val contentDescriptionResId: Int? = null,
    val iconAsset: String? = null,
)

@Composable
private fun SearchFiltersRow(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val items = remember {
        listOf(
            FilterChipItem(
                id = "sort",
                contentDescriptionResId = R.string.search_filter_sort,
                iconAsset = "icons/sort-by-up-02.svg"
            ),
            FilterChipItem(
                id = "filter",
                labelResId = R.string.search_filter_filters,
                contentDescriptionResId = R.string.search_filter_filters,
                iconAsset = "icons/filter-horizontal.svg"
            ),
            FilterChipItem(
                id = "new",
                labelResId = R.string.search_filter_only_new,
                contentDescriptionResId = R.string.search_filter_only_new,
            ),
            FilterChipItem(
                id = "delivery",
                labelResId = R.string.search_filter_only_delivery,
                contentDescriptionResId = R.string.search_filter_only_delivery,
            ),
        )
    }
    val selected = remember { mutableStateListOf<String>() }

    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(items, key = { it.id }) { item ->
            val isSelected = selected.contains(item.id)
            SearchFilterChip(
                item = item,
                selected = isSelected,
                onClick = {
                    if (isSelected) {
                        selected.remove(item.id)
                    } else {
                        selected.add(item.id)
                    }
                }
            )
        }
    }
}

@Composable
private fun SearchFilterChip(
    item: FilterChipItem,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val background = if (selected) Color(0xFF232323) else colorResource(id = R.color.chip_background)
    val contentColor = if (selected) Color.White else Color(0xFF727272)
    val imageLoader = rememberPricewiseImageLoader()
    val label = item.labelResId?.let { stringResource(id = it) }.orEmpty()
    val contentDescription = item.contentDescriptionResId?.let { stringResource(id = it) }.orEmpty()

    Surface(
        shape = RoundedCornerShape(30.dp),
        color = background,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (item.iconAsset != null) {
                SubcomposeAsyncImage(
                    model = "file:///android_asset/${item.iconAsset}",
                    imageLoader = imageLoader,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(20.dp),
                    colorFilter = ColorFilter.tint(contentColor),
                    contentScale = ContentScale.Fit
                )
                if (label.isNotBlank()) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
            if (label.isNotBlank()) {
                Text(
                    text = label,
                    style = MainTypography.ChipText,
                    color = contentColor
                )
            }
        }
    }
}
