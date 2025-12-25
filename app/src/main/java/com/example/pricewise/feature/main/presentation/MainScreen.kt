package com.example.pricewise.feature.main.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pricewise.R
import com.example.pricewise.feature.main.domain.model.Merchant
import com.example.pricewise.feature.main.domain.model.PopularQuery
import com.example.pricewise.feature.main.domain.model.ProductRecommendation
import com.example.pricewise.feature.main.domain.model.PromoBanner
import com.example.pricewise.feature.main.presentation.MainDimens.ContentHorizontalPadding
import com.example.pricewise.feature.main.presentation.MainDimens.OrangeHeaderHeight
import com.example.pricewise.feature.main.presentation.MainDimens.SearchBottomInset
import com.example.pricewise.feature.main.presentation.MainDimens.SearchTopInset
import com.example.pricewise.feature.main.presentation.MainDimens.SectionVerticalSpacing
import com.example.pricewise.feature.main.presentation.MainTypography
import com.example.pricewise.feature.main.presentation.components.BannerCarousel
import com.example.pricewise.feature.main.presentation.components.PopularQueriesSection
import com.example.pricewise.feature.main.presentation.components.PricewiseSearchBar
import com.example.pricewise.feature.main.presentation.components.RecommendationCard
import com.example.pricewise.ui.theme.PricewiseTheme

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = viewModel(),
    searchQueryOverride: String? = null,
    onSearchQueryChangeOverride: ((String) -> Unit)? = null,
    onSearchSubmitOverride: (() -> Unit)? = null,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    MainScreenContent(
        state = state,
        onQueryChange = onSearchQueryChangeOverride ?: viewModel::onSearchQueryChange,
        onSearchSubmit = onSearchSubmitOverride,
        onFavoriteToggle = viewModel::toggleFavorite,
        queryOverride = searchQueryOverride,
        modifier = modifier
    )
}

@Composable
private fun MainScreenContent(
    state: MainUiState,
    onQueryChange: (String) -> Unit,
    onFavoriteToggle: (ProductRecommendation) -> Unit,
    onSearchSubmit: (() -> Unit)? = null,
    queryOverride: String? = null,
    modifier: Modifier = Modifier,
) {
    val horizontalPadding = ContentHorizontalPadding
    val verticalSpacing = SectionVerticalSpacing

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(
            top = 0.dp,
            bottom = 32.dp
        ),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing)
    ) {
        item {
            HeroSearchBlock(
                query = queryOverride ?: state.searchQuery,
                onQueryChange = onQueryChange,
                onQueryClear = { onQueryChange("") },
                onSearchSubmit = onSearchSubmit,
                contentHorizontalPadding = horizontalPadding
            )
        }
        if (state.banners.isNotEmpty()) {
            item {
                BannerCarousel(
                    banners = state.banners,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = horizontalPadding)
                )
            }
        }
        if (state.popularQueries.isNotEmpty()) {
            item {
                PopularQueriesSection(
                    queries = state.popularQueries,
                    onQueryClick = { onQueryChange(it.query) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding),
                    contentPadding = PaddingValues(horizontal = 0.dp)
                )
            }
        }
        item {
            RecommendationsSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding),
                title = stringResource(id = R.string.recommendations_title),
                state = state,
                onFavoriteToggle = onFavoriteToggle
            )
        }
    }
}

@Composable
private fun HeroSearchBlock(
    query: String,
    onQueryChange: (String) -> Unit,
    onQueryClear: () -> Unit,
    onSearchSubmit: (() -> Unit)?,
    contentHorizontalPadding: Dp,
    modifier: Modifier = Modifier,
) {
    val headerHeight = OrangeHeaderHeight
    val orangeStart = colorResource(id = R.color.orange_gradient_start)
    val orangeEnd = colorResource(id = R.color.orange_gradient_end)
    val gradient = remember(orangeStart, orangeEnd) {
        Brush.linearGradient(
            colors = listOf(orangeStart, orangeEnd),
            start = Offset.Zero,
            end = Offset(900f, 260f)
        )
    }
    val heroShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 22.dp,
        bottomEnd = 22.dp
    )
    val topInset = 32.dp
    val bottomInset = SearchBottomInset

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(brush = gradient, shape = heroShape),
        contentAlignment = Alignment.TopCenter
    ) {
        PricewiseSearchBar(
            value = query,
            onValueChange = onQueryChange,
            onClear = onQueryClear,
            onSearch = onSearchSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = contentHorizontalPadding,
                    end = contentHorizontalPadding,
                    top = topInset,
                    bottom = bottomInset
                )
        )
    }
}

@Composable
private fun RecommendationsSection(
    title: String,
    state: MainUiState,
    onFavoriteToggle: (ProductRecommendation) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            style = MainTypography.SectionTitle,
            color = MaterialTheme.colorScheme.onBackground
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            state.recommendations.forEach { recommendation ->
                RecommendationCard(
                    recommendation = recommendation,
                    onFavoriteClick = { onFavoriteToggle(recommendation) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    PricewiseTheme {
        MainScreenContent(
            state = MainUiState(
                isLoading = false,
                searchQuery = "",
                banners = listOf(
                    PromoBanner(id = "1", title = "Как искать товары?", imageUrl = ""),
                    PromoBanner(id = "2", title = "Настройка поиска", imageUrl = ""),
                ),
                popularQueries = listOf(
                    PopularQuery(id = "iphone", query = "Iphone 16 pro"),
                    PopularQuery(id = "buy", query = "Лабубу купить"),
                    PopularQuery(id = "mic", query = "Fifine микрофон"),
                ),
                recommendations = listOf(
                    ProductRecommendation(
                        id = "1",
                        title = "Телефон Apple iPhone 16 Pro 128Gb Dual Sim",
                        price = 83980,
                        merchant = Merchant(id = "ozon", name = "ozon.ru", logoUrl = ""),
                        thumbnailUrl = "",
                        isFavorite = false
                    ),
                    ProductRecommendation(
                        id = "2",
                        title = "Клавиатура Nuphy AIR75v3 Wireless",
                        price = 13497,
                        merchant = Merchant(id = "wb", name = "wildberries.ru", logoUrl = ""),
                        thumbnailUrl = "",
                        isFavorite = false
                    )
                )
            ),
            onQueryChange = {},
            onFavoriteToggle = {}
        )
    }
}
