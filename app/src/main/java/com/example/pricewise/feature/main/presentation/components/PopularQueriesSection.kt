package com.example.pricewise.feature.main.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.pricewise.R
import com.example.pricewise.feature.main.domain.model.PopularQuery
import com.example.pricewise.feature.main.presentation.MainTypography

@Composable
fun PopularQueriesSection(
    queries: List<PopularQuery>,
    modifier: Modifier = Modifier,
    onQueryClick: (PopularQuery) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.popular_queries_title),
            style = MainTypography.SectionTitle,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = contentPadding
        ) {
            items(queries, key = { it.id }) { query ->
                val chipDescription = stringResource(
                    id = R.string.popular_query_chip_content_description,
                    query.query
                )
                Surface(
                    modifier = Modifier.semantics {
                        contentDescription = chipDescription
                    },
                    shape = RoundedCornerShape(30.dp),
                    color = colorResource(id = R.color.chip_background),
                    onClick = { onQueryClick(query) }
                ) {
                    Text(
                        text = query.query,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        style = MainTypography.ChipText,
                        color = androidx.compose.ui.graphics.Color(0xFF727272),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
