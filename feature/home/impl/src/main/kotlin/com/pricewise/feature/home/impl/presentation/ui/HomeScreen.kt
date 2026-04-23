package com.pricewise.feature.home.impl.presentation.ui

import LocalCustomColors
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pricewise.core.ui.components.PriceWiseSearchHeaderTokens
import com.pricewise.feature.home.impl.R
import com.pricewise.feature.home.impl.presentation.viewmodel.HomeScreenViewModel
import com.pricewise.feature.home.impl.presentation.ui.components.PopularQueriesRow
import com.pricewise.feature.home.impl.presentation.ui.components.ProductCard
import com.pricewise.feature.home.impl.presentation.ui.components.QuickActionCarousel
import com.pricewise.feature.home.impl.presentation.ui.placeholders.LoadingFeedSection
import com.pricewise.feature.home.impl.ui.theme.PriceWiseComposeTheme
import components.SearchBar

@Composable
fun HomeScreen(
    contentPadding: PaddingValues,
    modifier: Modifier,
    onSearchRequest: (String) -> Unit,
    onPhotoSearchRequest: () -> Unit,
) {
    val viewModel: HomeScreenViewModel = hiltViewModel()
    val state = viewModel.screenState.collectAsStateWithLifecycle().value

    HomeScreenContent(
        state = state,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onPhotoSearchClick = viewModel::onPhotoSearchClick,
        onQuickActionClick = viewModel::onQuickActionClick,
        onPopularQueryClick = viewModel::onPopularQueryClick,
        onProductFavoriteClick = viewModel::onProductFavoriteClick,
        onSearchRequest = onSearchRequest,
        onPhotoSearchRequest = onPhotoSearchRequest,
        contentPadding = contentPadding,
        modifier = modifier,
    )
}

@Composable
fun HomeScreenContent(
    state: HomeScreenState,
    onSearchQueryChange: (String) -> Unit,
    onPhotoSearchClick: () -> Unit,
    onQuickActionClick: (String) -> Unit,
    onPopularQueryClick: (String) -> Unit,
    onProductFavoriteClick: (String, String) -> Unit,
    onSearchRequest: (String) -> Unit,
    onPhotoSearchRequest: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(contentPadding),
        contentPadding = PaddingValues(bottom = HomeDimens.ListBottomPadding),
    ) {
        item {
            HeaderSection(
                query = when (state) {
                    HomeScreenState.Loading -> ""
                    is HomeScreenState.Error -> ""
                    is HomeScreenState.Loaded -> state.searchQuery
                },
                onQueryChange = onSearchQueryChange,
                onSearch = onSearchRequest,
                onPhotoSearchClick = {
                    onPhotoSearchClick()
                    onPhotoSearchRequest()
                },
                modifier = Modifier,
            )
        }

        when (state) {
            HomeScreenState.Loading -> {
                item {
                    Spacer(modifier = Modifier.height(HomeDimens.SectionContentSpacing))
                }
                item {
                    LoadingFeedSection(
                        modifier = Modifier,
                    )
                }
            }

            is HomeScreenState.Error -> {
                item {
                    Spacer(modifier = Modifier.height(HomeDimens.SectionContentSpacing))
                }
                item {
                    ErrorSection(
                        throwable = state.throwable,
                        modifier = Modifier,
                    )
                }
            }

            is HomeScreenState.Loaded -> {
                val loadedState = state

                if (
                    loadedState.quickActions.isNotEmpty() ||
                    loadedState.popularQueries.isNotEmpty() ||
                    loadedState.products.isNotEmpty()
                ) {
                    item {
                        Spacer(modifier = Modifier.height(HomeDimens.SectionContentSpacing))
                    }
                }

                if (loadedState.quickActions.isNotEmpty() || loadedState.popularQueries.isNotEmpty()) {
                    item {
                        PopularQueriesSection(
                            title = androidx.compose.ui.res.stringResource(R.string.home_popular_queries_title),
                            actions = loadedState.quickActions,
                            queries = loadedState.popularQueries,
                            onActionClick = onQuickActionClick,
                            onQueryClick = onPopularQueryClick,
                            modifier = Modifier,
                        )
                    }
                }

                if (
                    (loadedState.quickActions.isNotEmpty() || loadedState.popularQueries.isNotEmpty()) &&
                    loadedState.products.isNotEmpty()
                ) {
                    item {
                        Spacer(modifier = Modifier.height(HomeDimens.SectionContentSpacing))
                    }
                }

                if (loadedState.products.isNotEmpty()) {
                    item {
                        RecommendationsSectionTitle(
                            title = androidx.compose.ui.res.stringResource(R.string.home_recommendations_title),
                            modifier = Modifier,
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(HomeDimens.SectionContentSpacing))
                    }
                    itemsIndexed(
                        items = loadedState.products,
                        key = { _, product -> "${product.id}|${product.source}" },
                        contentType = { _, _ -> "product" },
                    ) { index, product ->
                        ProductCardListItem(
                            product = product,
                            onFavoriteClick = onProductFavoriteClick,
                            addTopSpacing = index > 0,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorSection(
    throwable: Throwable,
    modifier: Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = HomeDimens.ScreenHorizontalPadding),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = throwable.message.orEmpty().ifBlank { "Не удалось загрузить экран" },
            style = HomeTextStyles.ProductTitle,
            color = LocalCustomColors.current.midDark,
        )
    }
}

@Composable
private fun HeaderSection(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onPhotoSearchClick: () -> Unit,
    modifier: Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Brush.linearGradient(
                colors = listOf(Color(0xFFFFAB35), Color(0xFFFF2424)),
            ), PriceWiseSearchHeaderTokens.Shape),
    ) {
        SearchBar(
            value = query,
            onValueChange = onQueryChange,
            onClear = { onQueryChange("") },
            onSearch = { onSearch(query) },
            onPhotoSearchClick = onPhotoSearchClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = PriceWiseSearchHeaderTokens.HorizontalPadding,
                    end = PriceWiseSearchHeaderTokens.HorizontalPadding,
                    top = PriceWiseSearchHeaderTokens.TopPadding,
                    bottom = PriceWiseSearchHeaderTokens.BottomPadding,
                ),
        )
    }
}

@Composable
private fun PopularQueriesSection(
    title: String,
    actions: List<QuickActionUiModel>,
    queries: List<PopularQueryUiModel>,
    onActionClick: (String) -> Unit,
    onQueryClick: (String) -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(HomeDimens.SectionContentSpacing),
    ) {
        if (actions.isNotEmpty()) {
            QuickActionCarousel(
                actions = actions,
                onActionClick = onActionClick,
                modifier = Modifier,
            )
        }

        SectionTitle(
            title = title,
            modifier = Modifier.padding(horizontal = HomeDimens.ScreenHorizontalPadding),
        )

        if (queries.isNotEmpty()) {
            PopularQueriesRow(
                queries = queries,
                onQueryClick = onQueryClick,
                modifier = Modifier,
            )
        }
    }
}

@Composable
private fun RecommendationsSectionTitle(
    title: String,
    modifier: Modifier,
) {
    SectionTitle(
        title = title,
        modifier = modifier.padding(horizontal = HomeDimens.ScreenHorizontalPadding),
    )
}

@Composable
private fun ProductCardListItem(
    product: ProductUiModel,
    onFavoriteClick: (String, String) -> Unit,
    addTopSpacing: Boolean,
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        if (addTopSpacing) {
            Spacer(modifier = Modifier.height(HomeDimens.SectionContentSpacing))
        }
        ProductCard(
            product = product,
            onClick = {
                val productUrl = product.productUrl
                if (!productUrl.isNullOrBlank()) {
                    uriHandler.openUri(productUrl)
                }
            },
            onFavoriteClick = { onFavoriteClick(product.id, product.source) },
            modifier = Modifier.padding(horizontal = HomeDimens.ScreenHorizontalPadding),
        )
    }
}

@Composable
private fun SectionTitle(
    title: String,
    modifier: Modifier,
) {
    Text(
        text = title,
        modifier = modifier,
        style = HomeTextStyles.SectionTitle,
        color = LocalCustomColors.current.midDark,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 375, heightDp = 812)
@Composable
private fun HomeScreenPreview() {
    PriceWiseComposeTheme {
        HomeScreenContent(
            state = HomeScreenState.Loading,
            onSearchQueryChange = {},
            onPhotoSearchClick = {},
            onQuickActionClick = {},
            onPopularQueryClick = {},
            onProductFavoriteClick = { _, _ -> },
            onSearchRequest = {},
            onPhotoSearchRequest = {},
            contentPadding = PaddingValues(),
            modifier = Modifier,
        )
    }
}
