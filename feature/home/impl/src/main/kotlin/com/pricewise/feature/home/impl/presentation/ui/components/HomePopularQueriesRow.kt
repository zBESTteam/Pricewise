package com.pricewise.feature.home.impl.presentation.ui.components

import LocalCustomColors
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pricewise.feature.home.impl.presentation.ui.HomeColors
import com.pricewise.feature.home.impl.presentation.ui.HomeDimens
import com.pricewise.feature.home.impl.presentation.ui.HomeShapes
import com.pricewise.feature.home.impl.presentation.ui.HomeTextStyles
import com.pricewise.feature.home.impl.presentation.ui.PopularQueryUiModel

@Composable
internal fun PopularQueriesRow(
    queries: List<PopularQueryUiModel>,
    onQueryClick: (String) -> Unit,
    modifier: Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(HomeDimens.SmallSpacing),
        contentPadding = PaddingValues(horizontal = HomeDimens.ScreenHorizontalPadding),
    ) {
        items(queries, key = { query -> query.id }) { query ->
            QueryChip(
                query = query,
                onClick = { onQueryClick(query.id) },
                modifier = Modifier,
            )
        }
    }
}

@Composable
private fun QueryChip(
    query: PopularQueryUiModel,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    Surface(
        modifier = modifier
            .clip(HomeShapes.Chip)
            .clickable(onClick = onClick),
        color = LocalCustomColors.current.buttonColor,
        shape = HomeShapes.Chip,
    ) {
        Text(
            text = query.title,
            style = HomeTextStyles.Chip,
            color = LocalCustomColors.current.secondaryText,
            modifier = Modifier.padding(
                horizontal = HomeDimens.MediumSpacing,
                vertical = HomeDimens.ChipVerticalPadding,
            ),
        )
    }
}
