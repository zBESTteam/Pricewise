package com.example.pricewise.feature.search.presentation


import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pricewise.R
import com.example.pricewise.feature.main.presentation.MainDimens
import com.example.pricewise.feature.main.presentation.components.PricewiseSearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel,
) {
    val inter = FontFamily(
        Font(R.font.inter_regular, weight = FontWeight.W400),
        Font(R.font.inter_medium, weight = FontWeight.W500),
        Font(R.font.inter_semibold, weight = FontWeight.W600),
        Font(R.font.inter_bold, weight = FontWeight.W700),
    )
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showFilters by rememberSaveable { mutableStateOf(false) }
    val closeFilters = { showFilters = false }

    if (showFilters) {
        Filters(
            sheetState = sheetState,
            closeFilters = closeFilters,
            viewModel = viewModel
        )
    }

    Column(modifier = modifier) {
        val total = state.totalSources.coerceAtLeast(1)
        val checked = state.checkedSources.coerceAtLeast(0)
        val safeChecked = if (!state.isLoading && checked < total) total else checked
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            colorResource(R.color.start_gradient_color),
                            colorResource(R.color.end_gradient_color)
                        )
                    ), shape = RoundedCornerShape(bottomStart = 22.dp, bottomEnd = 22.dp)
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            PricewiseSearchBar(
                value = state.query,
                onValueChange = viewModel::onQueryChange,
                onClear = { viewModel.onQueryChange("") },
                onSearch = viewModel::submitSearch,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = MainDimens.ContentHorizontalPadding,
                        end = MainDimens.ContentHorizontalPadding,
                        top = 32.dp,
                        bottom = MainDimens.SearchBottomInset
                    )
            )
        }
        Text(
            modifier = Modifier.padding(
                start = SearchScreenDimensions.contentHorizontalPadding,
                top = 14.dp
            ),
            text = "Ищем товары",
            style = TextStyle(
                fontSize = 20.sp,
                lineHeight = 26.sp,
                fontFamily = inter,
                fontWeight = FontWeight(700),
                color = colorResource(R.color.mid_dark),
            )
        )
        Text(
            modifier = Modifier.padding(
                start = SearchScreenDimensions.contentHorizontalPadding,
                top = 7.dp
            ),
            text = "Проверили $safeChecked из $total магазинов",
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 21.sp,
                fontFamily = inter,
                fontWeight = FontWeight(600),
                color = colorResource(R.color.secondary_text_color),

                letterSpacing = 0.3.sp,
            )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .padding(horizontal = SearchScreenDimensions.contentHorizontalPadding)
                .padding(top = 7.dp)
                .background(
                    color = colorResource(R.color.button_color_search_screen),
                    shape = RoundedCornerShape(5.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .animateContentSize()
                    .width(
                        with(
                            ((screenWidthDp.dp - SearchScreenDimensions.contentHorizontalPadding * 2)) *
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
                .padding(top = 14.dp, start = SearchScreenDimensions.contentHorizontalPadding)
                .fillMaxWidth()
                .height(44.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            item {
                DefaultButton(icon = R.drawable.icon_sort, isSelected = false, onClick = {})
            }
            item {
                DefaultButton(icon = R.drawable.icon_filter, isSelected = false, onClick = { showFilters = true })
            }
            item {
                DefaultButton(text = "Только новые", isSelected = false, onClick = {})
            }
            item {
                DefaultButton(text = "Только c доставкой", isSelected = false, onClick = {})
            }
        }
        if (state.items.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = SearchScreenDimensions.contentHorizontalPadding)
                    .fillMaxSize()
                    .padding(top = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(
                    items = state.items,
                    key = { it.id }
                ) { item ->
                    ProductCard(item, {})
                }
            }
        }
        if (state.items.isEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = SearchScreenDimensions.contentHorizontalPadding)
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
fun DefaultButton(
    text: String? = null,
    icon: Int? = null,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var modifier = Modifier
        .height(41.dp)
        .background(
            color = if (!isSelected) colorResource(R.color.disabled_filter_button_color) else colorResource(
                R.color.mid_dark
            ),
            shape = RoundedCornerShape(size = 14.dp)
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
                tint = colorResource(R.color.icons_color),
                contentDescription = null
            )
        if (text != null)
            Text(
                modifier = Modifier.padding(all = 10.dp),
                text = text,
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 21.sp,
                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                    fontWeight = FontWeight(600),
                    color = if (!isSelected) colorResource(R.color.disabled_filter_button_text_color) else colorResource(
                        R.color.white
                    ),
                    letterSpacing = 0.3.sp,
                )
            )
    }
}