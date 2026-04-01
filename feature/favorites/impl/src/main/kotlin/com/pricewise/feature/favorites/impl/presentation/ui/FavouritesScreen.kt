package com.pricewise.feature.favorites.impl.presentation.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pricewise.core.ui.components.PriceWiseProductCard
import com.pricewise.feature.favorites.impl.R
import com.pricewise.feature.favorites.impl.presentation.ui.components.DefaultButton
import com.pricewise.feature.favorites.impl.presentation.viewmodel.FavouritesViewModel

@Composable
fun FavoritesScreen(
    contentPadding: PaddingValues = PaddingValues(),
) {
    val inter = remember {
        FontFamily(
            Font(R.font.inter_regular, weight = FontWeight.W400),
            Font(R.font.inter_medium, weight = FontWeight.W500),
            Font(R.font.inter_semibold, weight = FontWeight.W600),
            Font(R.font.inter_bold, weight = FontWeight.W700),
        )
    }

    val viewModel: FavouritesViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    Column(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
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
            bottomStart = 22.dp,
            bottomEnd = 22.dp
        )

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
                fontFamily = inter,
                fontWeight = FontWeight(700),
                color = colorResource(R.color.mid_dark),
            )
        )

        Row(modifier = Modifier.padding(horizontal = 15.dp)) {
            DefaultButton(icon = R.drawable.icon_sort, isSelected = false, onClick = {})

            DefaultButton(icon = R.drawable.icon_filter, isSelected = false, onClick = {})

            DefaultButton(
                text = stringResource(R.string.sort_by_brand),
                isSelected = false,
                onClick = {})
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(color = orangeStart)
                }

                uiState.error != null -> {
                    Text(text = uiState.error ?: "Ошибка", color = Color.Red)
                }

                uiState.items.isEmpty() -> {
                    Text(
                        text = stringResource(R.string.favorites_list_is_empty),
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.items, key = { it.id }) { product ->
                            PriceWiseProductCard(
                                product = product,
                                onFavoriteClick = { viewModel.removeFavorite(product) },
                                onClick = {TODO()},
                                modifier = Modifier
                            )
                        }
                    }
                }
            }
        }
    }
}