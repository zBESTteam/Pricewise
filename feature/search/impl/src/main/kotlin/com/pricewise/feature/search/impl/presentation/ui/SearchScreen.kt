package com.pricewise.feature.search.impl.presentation.ui

import LocalCustomColors
import Typography.Inter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pricewise.feature.search.impl.presentation.components.ProductCard
import com.pricewise.feature.search.impl.presentation.viewmodel.SearchViewModel
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.pricewise.core.ui.components.PriceWiseSearchHeaderTokens
import com.pricewise.core.ui.R
import com.pricewise.feature.search.impl.presentation.components.ProductCardShimmer
import components.SearchBar

@Composable
fun SearchScreen(
    contentPadding: PaddingValues,
    modifier: Modifier,
    initialQuery: String,
) {
    val viewModel: SearchViewModel = hiltViewModel()
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var filtersSelected by rememberSaveable {
        mutableStateOf(
            viewModel.filtersState.value != FiltersState(
                priceFrom = 0L,
                priceTo = 0L
            )
        )
    }
    var onlyDeliverySelected by rememberSaveable { mutableStateOf(false) }
    var onlyNewSelected by rememberSaveable { mutableStateOf(false) }
    var sortSelected by rememberSaveable { mutableStateOf(false) }
    var showFilters by rememberSaveable { mutableStateOf(false) }

    val closeFilters = { showFilters = false }

    if (showFilters) {
        Filters(
            closeFilters = closeFilters,
            viewModel = viewModel
        )
    }

    LaunchedEffect(initialQuery) {
        viewModel.initializeSearch(searchQuery = initialQuery)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .background(MaterialTheme.colorScheme.background),
    ) {
        val total = state.totalSources.coerceAtLeast(1)
        val checked = state.checkedSources.coerceAtLeast(0)
        val safeChecked = if (!state.isLoading && checked < total) total else checked
        SearchHeaderSection(
            query = state.query,
            onQueryChange = viewModel::onQueryChange,
            onSearch = viewModel::submitSearch,
            onClear = { viewModel.clearQuery() },
            onPhotoSearchClick = {},
            modifier = Modifier,
        )
        Text(
            modifier = Modifier.padding(
                start = 15.dp,
                top = 14.dp
            ),
            text = "Ищем товары",
            style = TextStyle(
                fontSize = 20.sp,
                lineHeight = 26.sp,
                fontFamily = Inter,
                fontWeight = FontWeight(700),
                color = LocalCustomColors.current.midDark,
            )
        )
        Text(
            modifier = Modifier.padding(
                start = 15.dp,
                top = 7.dp
            ),
            text = "Проверили $safeChecked из $total магазинов",
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 21.sp,
                fontFamily = Inter,
                fontWeight = FontWeight(600),
                color = LocalCustomColors.current.secondaryText,

                letterSpacing = 0.3.sp,
            )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .padding(horizontal = 15.dp)
                .padding(top = 7.dp)
                .background(
                    color = LocalCustomColors.current.buttonColor,
                    shape = RoundedCornerShape(5.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .animateContentSize()
                    .width(
                        with(
                            ((screenWidthDp.dp - 15.dp * 2)) *
                                    safeChecked / total
                        ) {
                            if (this == 0.dp) 16.dp
                            else this
                        }
                    )
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                colorResource(R.color.start_gradient_color),
                                colorResource(R.color.end_gradient_color)
                            )
                        ), shape = RoundedCornerShape(5.dp)
                    )
            )
        }
        LazyRow(
            modifier = Modifier
                .padding(top = 14.dp, start = 15.dp)
                .fillMaxWidth()
                .height(44.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            item {
                DefaultButton(icon = R.drawable.ic_sort, isSelected = sortSelected, onClick = {})
            }
            item {
                DefaultButton(
                    icon = R.drawable.ic_filter,
                    isSelected = filtersSelected,
                    onClick = { showFilters = true })
            }
            item {
                DefaultButton(
                    text = stringResource(com.pricewise.feature.search.impl.R.string.only_new),
                    isSelected = onlyNewSelected,
                    onClick = {
                        if (!onlyNewSelected) {
                            viewModel.setOnlyNew(true)
                            viewModel.performSearch(viewModel.uiState.value.query)
                        }
                        onlyNewSelected = !onlyNewSelected
                    })
            }
            item {
                DefaultButton(
                    text = stringResource(com.pricewise.feature.search.impl.R.string.only_with_delievery),
                    isSelected = onlyDeliverySelected,
                    onClick = {
                        if (viewModel.filtersState.value.deliveryChosen == Delivery.ANY && !onlyDeliverySelected) {
                            viewModel.setDeliveryChosen(
                                Delivery.EXIST
                            )
                            viewModel.performSearch(viewModel.uiState.value.query)
                        }
                        onlyDeliverySelected = !onlyDeliverySelected
                    })
            }
        }
        if (state.items.isNotEmpty() && !state.isLoading) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxSize()
                    .padding(top = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(
                    items = state.items,
                    key = { it.id }
                ) { item ->
                    ProductCard(
                        product = item,
                        addToFavourites = { product ->
                            viewModel.onProductFavoriteClick(productId = product.id)
                        },
                    )
                }
            }
        }
        if (state.isLoading) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxSize()
                    .padding(top = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(
                    100
                ) {
                    ProductCardShimmer()
                }
            }
        }
    }
}

@Composable
private fun SearchHeaderSection(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    onSearch: () -> Unit,
    onPhotoSearchClick: () -> Unit,
    modifier: Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colorResource(R.color.start_gradient_color),
                        colorResource(R.color.end_gradient_color),
                    ),
                ),
                shape = PriceWiseSearchHeaderTokens.Shape,
            ),
        contentAlignment = Alignment.TopCenter,
    ) {
        SearchBar(
            value = query,
            onValueChange = onQueryChange,
            onClear = onClear,
            onSearch = onSearch,
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
fun DefaultButton(
    text: String? = null,
    icon: Int? = null,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var modifier = Modifier
        .height(41.dp)
        .clip(shape = RoundedCornerShape(size = 14.dp))
        .background(
            color = if (!isSelected) LocalCustomColors.current.disabledFilterButtonColor else LocalCustomColors.current.midDark
        )
        .clickable { onClick() }
    if (icon != null && text == null) modifier = modifier.width(41.dp)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        if (icon != null)
            Icon(
                modifier = Modifier.padding(all = 10.dp),
                painter = painterResource(icon),
                tint = LocalCustomColors.current.iconsColor,
                contentDescription = null
            )
        if (text != null)
            Text(
                modifier = Modifier.padding(all = 10.dp),
                text = text,
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 21.sp,
                    fontFamily = Inter,
                    fontWeight = FontWeight(600),
                    color = if (!isSelected) LocalCustomColors.current.disabledFilterButtonTextColor else MaterialTheme.colorScheme.onPrimary,
                    letterSpacing = 0.3.sp,
                )
            )
    }
}