package com.pricewise.app.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.pricewise.core.ui.R as CoreUiR

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
                .padding(
                    start = BottomBarTokens.HorizontalPadding,
                    top = BottomBarTokens.TopPadding,
                    end = BottomBarTokens.HorizontalPadding,
                    bottom = BottomBarTokens.BottomPadding,
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            destinations.forEach { destination ->
                BottomBarItem(
                    destination = destination,
                    selected = destination == currentDestination,
                    onClick = { onDestinationSelected(destination) },
                    modifier = Modifier,
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
    val interactionSource = remember { MutableInteractionSource() }
    val iconResId = when {
        destination == PriceWiseTopLevelDestination.Home && selected -> CoreUiR.drawable.ic_search_clicked
        destination == PriceWiseTopLevelDestination.Home -> CoreUiR.drawable.ic_search
        destination == PriceWiseTopLevelDestination.Favorites && selected -> CoreUiR.drawable.ic_favorite
        destination == PriceWiseTopLevelDestination.Favorites -> CoreUiR.drawable.ic_favorite_disabled
        destination == PriceWiseTopLevelDestination.Profile && selected -> CoreUiR.drawable.ic_profile_clicked
        else -> CoreUiR.drawable.ic_profile
    }
    val iconWidth = when (destination) {
        PriceWiseTopLevelDestination.Home -> BottomBarTokens.SearchIconWidth
        PriceWiseTopLevelDestination.Favorites -> BottomBarTokens.FavoritesIconWidth
        PriceWiseTopLevelDestination.Profile -> BottomBarTokens.ProfileIconWidth
    }
    val iconHeight = when (destination) {
        PriceWiseTopLevelDestination.Home -> BottomBarTokens.SearchIconHeight
        PriceWiseTopLevelDestination.Favorites -> BottomBarTokens.FavoritesIconHeight
        PriceWiseTopLevelDestination.Profile -> BottomBarTokens.ProfileIconHeight
    }

    Box(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                role = Role.Tab,
            )
            .semantics {
                this.role = Role.Tab
                this.contentDescription = contentDescription
            },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(iconResId),
            contentDescription = null,
            modifier = Modifier
                .width(iconWidth)
                .height(iconHeight),
        )
    }
}

private object BottomBarTokens {
    val CornerRadius = 28.dp
    val HorizontalPadding = 64.dp
    val TopPadding = 24.dp
    val BottomPadding = 40.dp
    val SearchIconWidth = 21.dp
    val SearchIconHeight = 21.dp
    val FavoritesIconWidth = 19.dp
    val FavoritesIconHeight = 17.dp
    val ProfileIconWidth = 18.dp
    val ProfileIconHeight = 18.dp
}
