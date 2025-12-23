package com.example.pricewise.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import coil.compose.SubcomposeAsyncImage
import com.example.pricewise.AppDestinations
import com.example.pricewise.core.ui.rememberPricewiseImageLoader

@Composable
fun PricewiseBottomBar(
    currentDestination: NavDestination?,
    onNavigate: (AppDestinations) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navShape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp)
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(navShape),
        shape = navShape,
        color = Color.White,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.height(84.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppDestinations.entries.forEach { destination ->
                val selected = currentDestination.isDestinationSelected(destination)
                val interactionSource = remember(destination.route) { MutableInteractionSource() }
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = { onNavigate(destination) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    NavBarIcon(
                        assetPath = if (selected) {
                            destination.activeIconAsset
                        } else {
                            destination.inactiveIconAsset
                        },
                        contentDescription = stringResource(destination.contentDescriptionId)
                    )
                }
            }
        }
    }
}

private fun NavDestination?.isDestinationSelected(destination: AppDestinations): Boolean {
    return this?.hierarchy?.any { it.route == destination.route } == true
}

@Composable
private fun NavBarIcon(
    assetPath: String,
    contentDescription: String,
) {
    val imageLoader = rememberPricewiseImageLoader()
    SubcomposeAsyncImage(
        model = "file:///android_asset/$assetPath",
        imageLoader = imageLoader,
        contentDescription = contentDescription,
        modifier = Modifier.size(24.dp),
        contentScale = ContentScale.Fit,
    )
}
