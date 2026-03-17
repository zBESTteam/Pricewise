package com.pricewise.app.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun PriceWiseBottomBar(
    destinations: List<PriceWiseTopLevelDestination>,
    currentDestination: PriceWiseTopLevelDestination?,
    onDestinationSelected: (PriceWiseTopLevelDestination) -> Unit,
    modifier: Modifier,
) {
    val navShape = RoundedCornerShape(
        topStart = BottomBarTokens.CornerRadius,
        topEnd = BottomBarTokens.CornerRadius,
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(navShape),
        shape = navShape,
        color = Color.White,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(BottomBarTokens.BarHeight)
                .padding(
                    horizontal = BottomBarTokens.HorizontalPadding,
                    vertical = BottomBarTokens.VerticalPadding,
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            destinations.forEach { destination ->
                BottomBarItem(
                    destination = destination,
                    selected = destination == currentDestination,
                    onClick = { onDestinationSelected(destination) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    destination: PriceWiseTopLevelDestination,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    val contentDescription = stringResource(destination.contentDescriptionRes)

    Box(
        modifier = modifier
            .height(BottomBarTokens.ItemHeight)
            .clip(RoundedCornerShape(BottomBarTokens.ItemCornerRadius))
            .clickable(
                onClick = onClick,
                role = Role.Tab,
            )
            .semantics {
                this.role = Role.Tab
                this.contentDescription = contentDescription
            },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = when (destination) {
                PriceWiseTopLevelDestination.Home -> Icons.Outlined.Search
                PriceWiseTopLevelDestination.Favorites -> Icons.Outlined.FavoriteBorder
                PriceWiseTopLevelDestination.Profile -> Icons.Outlined.Person
            },
            contentDescription = null,
            modifier = Modifier.size(BottomBarTokens.IconSize),
            tint = if (selected) {
                BottomBarTokens.SelectedIconColor
            } else {
                BottomBarTokens.UnselectedIconColor
            },
        )
    }
}

private object BottomBarTokens {
    val BarHeight = 80.dp
    val CornerRadius = 28.dp
    val HorizontalPadding = 24.dp
    val VerticalPadding = 12.dp
    val ItemHeight = 48.dp
    val ItemCornerRadius = 16.dp
    val IconSize = 24.dp
    val SelectedIconColor = Color(0xFFFF6D1F)
    val UnselectedIconColor = Color(0xFF292929)
}
