package com.pricewise.feature.home.impl.presentation.ui.placeholders

import LocalCustomColors
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import com.pricewise.feature.home.impl.presentation.ui.HomeLoadingDefaults

@Composable
internal fun PlaceholderBox(
    modifier: Modifier,
    shape: Shape?,
    delayMillis: Int,
    useShimmer: Boolean,
) {
    val shapedModifier = if (shape == null) {
        modifier
    } else {
        modifier.clip(shape)
    }

    Box(
        modifier = shapedModifier
            .background(LocalCustomColors.current.shimmer1)
            .then(
                if (useShimmer) {
                    Modifier.shimmerLoading(shape = shape, delayMillis = delayMillis)
                } else {
                    Modifier
                },
            ),
    )
}

private fun Modifier.shimmerLoading(
    shape: Shape?,
    delayMillis: Int,
): Modifier = composed {
    val shimmerTransition = rememberInfiniteTransition(label = "placeholder_transition")
    val shimmerShift by shimmerTransition.animateFloat(
        initialValue = HomeLoadingDefaults.ShimmerStartOffset,
        targetValue = HomeLoadingDefaults.ShimmerTargetOffset,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = HomeLoadingDefaults.ShimmerDurationMillis,
                delayMillis = delayMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "placeholder_shift",
    )
    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            LocalCustomColors.current.shimmer1,
            LocalCustomColors.current.shimmer2,
            LocalCustomColors.current.shimmer1
        ),
        start = Offset(shimmerShift, 0f),
        end = Offset(
            shimmerShift + HomeLoadingDefaults.ShimmerHighlightSize,
            HomeLoadingDefaults.ShimmerHighlightSize,
        ),
    )

    if (shape == null) {
        background(brush = shimmerBrush)
    } else {
        background(brush = shimmerBrush, shape = shape)
    }
}
