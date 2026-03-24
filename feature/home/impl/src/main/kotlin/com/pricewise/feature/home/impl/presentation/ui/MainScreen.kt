package com.pricewise.feature.home.impl.presentation.ui

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pricewise.feature.home.impl.R
import com.pricewise.feature.home.impl.presentation.ui.components.PopularQueriesRow
import com.pricewise.feature.home.impl.presentation.ui.components.ProductCard
import com.pricewise.feature.home.impl.presentation.ui.components.QuickActionCarousel
import com.pricewise.feature.home.impl.presentation.ui.components.SearchBar
import com.pricewise.feature.home.impl.presentation.ui.placeholders.LoadingFeedSection
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
            .background(HomeColors.ScreenBackground)
            .padding(contentPadding),
        contentPadding = PaddingValues(bottom = HomeDimens.ListBottomPadding),
    ) {
        item {
            HeaderSection(
                query = state.searchQuery,
                onQueryChange = onSearchQueryChange,
                onPhotoSearchClick = onPhotoSearchClick,
                modifier = Modifier,
            )
        }

        if (shouldShowLoadingState ||
            state.quickActions.isNotEmpty() ||
            state.popularQueries.isNotEmpty() ||
            state.products.isNotEmpty()
        ) {
            item {
                Spacer(modifier = Modifier.height(HomeDimens.SectionContentSpacing))
            }
        }

        if (shouldShowLoadingState) {
            item {
                LoadingFeedSection(
                    modifier = Modifier,
                )
            }
        } else {
            if (state.quickActions.isNotEmpty() || state.popularQueries.isNotEmpty()) {
                item {
                    PopularQueriesSection(
                        title = androidx.compose.ui.res.stringResource(R.string.home_popular_queries_title),
                        actions = state.quickActions,
                        queries = state.popularQueries,
                        onActionClick = onQuickActionClick,
                        onQueryClick = onPopularQueryClick,
                        modifier = Modifier,
                    )
                }
            }

            if ((state.quickActions.isNotEmpty() || state.popularQueries.isNotEmpty()) &&
                state.products.isNotEmpty()
            ) {
                item {
                    Spacer(modifier = Modifier.height(HomeDimens.SectionContentSpacing))
                }
            }

            if (state.products.isNotEmpty()) {
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
                    items = state.products,
                    key = { _, product -> product.id },
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

@Composable
private fun HeaderSection(
    query: String,
    onQueryChange: (String) -> Unit,
    onPhotoSearchClick: () -> Unit,
    modifier: Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(HomeColors.HeaderGradient, HomeShapes.Header),
    ) {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onPhotoSearchClick = onPhotoSearchClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = HomeDimens.ScreenHorizontalPadding,
                    end = HomeDimens.ScreenHorizontalPadding,
                    top = HomeDimens.HeaderTopPadding,
                    bottom = HomeDimens.HeaderBottomPadding,
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
    onFavoriteClick: (String) -> Unit,
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
            onFavoriteClick = { onFavoriteClick(product.id) },
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
        color = HomeColors.SectionTitle,
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
