package com.pricewise.feature.favorites.impl.presentation.ui

import LocalCustomColors
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pricewise.core.ui.components.FilterActionButton
import com.pricewise.core.ui.components.PriceWiseProductCard
import com.pricewise.feature.favorites.impl.R
import com.pricewise.feature.favorites.impl.presentation.viewmodel.FavoritesSortOption
import com.pricewise.feature.favorites.impl.presentation.viewmodel.FavouritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    contentPadding: PaddingValues = PaddingValues(),
) {
    val viewModel: FavouritesViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showSortSheet by rememberSaveable { mutableStateOf(false) }
    var showFilterSheet by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (showSortSheet) {
        SortBottomSheet(
            selectedSort = uiState.sortOption,
            onSelect = { option ->
                viewModel.setSortOption(option)
                showSortSheet = false
            },
            onDismiss = { showSortSheet = false },
        )
    }

    if (showFilterSheet) {
        val searchFilterViewModel: SearchViewModel = hiltViewModel()
        LaunchedEffect(
            uiState.onlyMarketplaces,
            uiState.onlyOfflineShops,
            uiState.priceFrom,
            uiState.priceTo,
        ) {
            searchFilterViewModel.setIsProductChosen(true)
            searchFilterViewModel.setOnlyMarketplaces(uiState.onlyMarketplaces)
            searchFilterViewModel.setOnlyOfflineShops(uiState.onlyOfflineShops)
            searchFilterViewModel.setPriceFrom(uiState.priceFrom)
            searchFilterViewModel.setPriceTo(uiState.priceTo)
        }
        Filters(
            closeFilters = {
                viewModel.setOnlyMarketplaces(searchFilterViewModel.filtersState.value.onlyMarketplaces)
                viewModel.setOnlyOfflineShops(searchFilterViewModel.filtersState.value.onlyOfflineShops)
                viewModel.setPriceRange(
                    from = searchFilterViewModel.filtersState.value.priceFrom,
                    to = searchFilterViewModel.filtersState.value.priceTo,
                )
                showFilterSheet = false
            },
            viewModel = searchFilterViewModel
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
    ) {
        val orangeStart = colorResource(id = R.color.orange_gradient_start)
        val orangeEnd = colorResource(id = R.color.orange_gradient_end)
        val density = LocalDensity.current
        val statusBarHeight = with(density) {
            WindowInsets.safeDrawing.getTop(this).toDp()
        }
        val gradient = remember(orangeStart, orangeEnd) {
            Brush.linearGradient(
                colors = listOf(orangeStart, orangeEnd),
                start = Offset.Zero,
                end = Offset(900f, 260f),
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(brush = gradient, shape = heroShape),
            contentAlignment = Alignment.Center
        ) {}

        Text(
            modifier = Modifier.padding(
                start = 15.dp,
                top = 15.dp,
                bottom = 15.dp
            ),
            text = stringResource(R.string.favorites),
            style = TextStyle(
                fontSize = 20.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight(700),
                color = LocalCustomColors.current.midDark,
            )
        )

        Row(modifier = Modifier.padding(horizontal = 15.dp), horizontalArrangement = Arrangement.spacedBy(18.dp)) {
            FilterActionButton(
                icon = com.pricewise.core.ui.R.drawable.ic_sort,
                isSelected = false,
                onClick = { showSortSheet = true },
            )
            FilterActionButton(
                icon = com.pricewise.core.ui.R.drawable.ic_filter,
                isSelected = uiState.onlyMarketplaces || uiState.onlyOfflineShops || uiState.priceFrom > 0 || uiState.priceTo > 0,
                onClick = {
                    showFilterSheet = true
                },
            )
            FilterActionButton(
                text = stringResource(R.string.sort_by_brand),
                isSelected = uiState.sortOption == FavoritesSortOption.BRAND_ASC,
                onClick = {
                    viewModel.setSortOption(
                        if (uiState.sortOption == FavoritesSortOption.BRAND_ASC) {
                            FavoritesSortOption.NONE
                        } else {
                            FavoritesSortOption.BRAND_ASC
                        },
                    )
                },
            )
        }

                FilterActionButton(
                    text = stringResource(R.string.sort_by_brand),
                    isSelected = uiState.sortOption == FavoritesSortOption.BRAND_ASC,
                    onClick = {
                        viewModel.setSortOption(
                            if (uiState.sortOption == FavoritesSortOption.BRAND_ASC) {
                                FavoritesSortOption.NONE
                            } else {
                                FavoritesSortOption.BRAND_ASC
                            },
                        )
                    },
                )
            }

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator(color = orangeStart)
                    }

                uiState.items.isEmpty() -> {
                    Text(
                        text = stringResource(R.string.favorites_list_is_empty),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                    else -> {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(uiState.items) { product ->
                                PriceWiseProductCard(
                                    product = product,
                                    onFavoriteClick = { viewModel.removeFavorite(product) },
                                    onClick = {},
                                    modifier = Modifier
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SortBottomSheet(
    selectedSort: FavoritesSortOption,
    onSelect: (FavoritesSortOption) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(text = stringResource(R.string.sort_by_price))
            SortOptionRow(
                title = stringResource(R.string.sort_by_price_asc),
                selected = selectedSort == FavoritesSortOption.PRICE_ASC,
                onClick = { onSelect(FavoritesSortOption.PRICE_ASC) },
            )
            SortOptionRow(
                title = stringResource(R.string.sort_by_price_desc),
                selected = selectedSort == FavoritesSortOption.PRICE_DESC,
                onClick = { onSelect(FavoritesSortOption.PRICE_DESC) },
            )
            SortOptionRow(
                title = stringResource(R.string.sort_by_brand),
                selected = selectedSort == FavoritesSortOption.BRAND_ASC,
                onClick = { onSelect(FavoritesSortOption.BRAND_ASC) },
            )
            SortOptionRow(
                title = stringResource(R.string.sort_reset),
                selected = selectedSort == FavoritesSortOption.NONE,
                onClick = { onSelect(FavoritesSortOption.NONE) },
            )
        }
    }
}

@Composable
private fun SortOptionRow(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    FilterOptionRow(title = title, selected = selected, onClick = onClick)
}

@Composable
private fun FilterOptionRow(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor = if (!selected) colorResource(R.color.disabled_filter_button_color) else Color.Transparent
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, shape = RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoritesFilterSheet(
    onlyMarketplaces: Boolean,
    onlyOfflineShops: Boolean,
    priceFrom: Long,
    priceTo: Long,
    onApply: (marketplaces: Boolean, offlineShops: Boolean, from: Long, to: Long) -> Unit,
    onDismiss: () -> Unit,
) {
    var localMarketplaces by rememberSaveable { mutableStateOf(onlyMarketplaces) }
    var localOfflineShops by rememberSaveable { mutableStateOf(onlyOfflineShops) }
    var localPriceFrom by rememberSaveable { mutableStateOf(priceFrom.toString()) }
    var localPriceTo by rememberSaveable { mutableStateOf(priceTo.toString()) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.favorites_filter_title),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight(700),
                    color = colorResource(R.color.mid_dark),
                ),
            )

            FilterOptionRow(
                title = stringResource(R.string.favorites_filter_marketplaces),
                selected = localMarketplaces,
                onClick = {
                    localMarketplaces = !localMarketplaces
                    if (localMarketplaces && localOfflineShops) localOfflineShops = false
                },
            )
            FilterOptionRow(
                title = stringResource(R.string.favorites_filter_offline),
                selected = localOfflineShops,
                onClick = {
                    localOfflineShops = !localOfflineShops
                    if (localOfflineShops && localMarketplaces) localMarketplaces = false
                },
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .background(
                        color = colorResource(R.color.mid_dark),
                        shape = RoundedCornerShape(14.dp),
                    )
                    .clickable {
                        onApply(
                            localMarketplaces,
                            localOfflineShops,
                            localPriceFrom.toLongOrNull() ?: 0L,
                            localPriceTo.toLongOrNull() ?: 0L,
                        )
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.favorites_filter_apply),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(600),
                        color = MaterialTheme.colorScheme.onPrimary,
                    ),
                )
            }
        }
    }
}
