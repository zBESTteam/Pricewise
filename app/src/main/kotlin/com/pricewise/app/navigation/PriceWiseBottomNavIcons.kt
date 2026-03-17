package com.pricewise.app.navigation

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.PathParser

@Composable
internal fun SearchBottomNavIcon(
    isSelected: Boolean,
    modifier: Modifier,
) {
    Canvas(modifier = modifier) {
        val brush = priceWiseGradientBrush(SearchViewportWidth, SearchViewportHeight)
        withTransform({
            val horizontalInset = SearchStrokeWidth / 2f
            val verticalInset = SearchStrokeWidth / 2f
            translate(left = horizontalInset, top = verticalInset)
            scale(
                scaleX = (size.width - horizontalInset * 2f) / SearchViewportWidth,
                scaleY = (size.height - verticalInset * 2f) / SearchViewportHeight,
            )
        }) {
            if (isSelected) {
                drawPath(
                    path = SearchFillPath,
                    brush = brush,
                )
            }
            drawPath(
                path = SearchStrokePath,
                brush = if (isSelected) brush else solidDarkBrush,
                style = Stroke(
                    width = SearchStrokeWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                ),
            )
        }
    }
}

@Composable
internal fun HeartBottomNavIcon(
    isSelected: Boolean,
    modifier: Modifier,
) {
    Canvas(modifier = modifier) {
        val brush = priceWiseGradientBrush(HeartViewportWidth, HeartViewportHeight)
        withTransform({
            val horizontalInset = HeartStrokeWidth / 2f
            val verticalInset = HeartStrokeWidth / 2f
            translate(left = horizontalInset, top = verticalInset)
            scale(
                scaleX = (size.width - horizontalInset * 2f) / HeartViewportWidth,
                scaleY = (size.height - verticalInset * 2f) / HeartViewportHeight,
            )
        }) {
            if (isSelected) {
                drawPath(
                    path = HeartSelectedPath,
                    brush = brush,
                )
                drawPath(
                    path = HeartSelectedPath,
                    brush = brush,
                    style = Stroke(
                        width = HeartStrokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round,
                    ),
                )
            } else {
                drawPath(
                    path = HeartOutlinePath,
                    color = BottomNavIconColors.IconDefault,
                    style = Stroke(
                        width = HeartStrokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round,
                    ),
                )
            }
        }
    }
}

@Composable
internal fun ProfileBottomNavIcon(
    isSelected: Boolean,
    modifier: Modifier,
) {
    Canvas(modifier = modifier) {
        if (isSelected) {
            withTransform({
                val inset = ProfileSelectedInset
                translate(left = inset, top = inset)
                scale(
                    scaleX = (size.width - inset * 2f) / ProfileSelectedViewport,
                    scaleY = (size.height - inset * 2f) / ProfileSelectedViewport,
                )
            }) {
                drawPath(
                    path = ProfileSelectedBodyPath,
                    brush = priceWiseGradientBrush(ProfileSelectedViewport, ProfileSelectedViewport),
                )
                drawCircle(
                    brush = priceWiseGradientBrush(ProfileSelectedViewport, ProfileSelectedViewport),
                    radius = ProfileSelectedCircleRadius,
                    center = ProfileSelectedCircleCenter,
                )
            }
        } else {
            withTransform({
                val inset = ProfileOutlineStrokeWidth / 2f
                translate(left = inset, top = inset)
                scale(
                    scaleX = (size.width - inset * 2f) / ProfileOutlineViewport,
                    scaleY = (size.height - inset * 2f) / ProfileOutlineViewport,
                )
            }) {
                drawPath(
                    path = ProfileOutlineBodyPath,
                    color = BottomNavIconColors.IconDefault,
                    style = Stroke(
                        width = ProfileOutlineStrokeWidth,
                        join = StrokeJoin.Round,
                    ),
                )
                drawCircle(
                    color = BottomNavIconColors.IconDefault,
                    radius = ProfileOutlineCircleRadius,
                    center = ProfileOutlineCircleCenter,
                    style = Stroke(width = ProfileOutlineStrokeWidth),
                )
            }
        }
    }
}

private fun priceWiseGradientBrush(
    viewportWidth: Float,
    viewportHeight: Float,
): Brush {
    return Brush.linearGradient(
        colors = BottomNavIconColors.Gradient,
        start = Offset(0f, 0f),
        end = Offset(viewportWidth, viewportHeight * 0.9f),
    )
}

private val solidDarkBrush = Brush.linearGradient(
    colors = listOf(BottomNavIconColors.IconDefault, BottomNavIconColors.IconDefault),
)

private object BottomNavIconColors {
    val IconDefault = Color(0xFF292929)
    val Gradient = listOf(Color(0xFFFFAB35), Color(0xFFE62727))
}

private const val SearchViewportWidth = 24f
private const val SearchViewportHeight = 24f
private const val SearchStrokeWidth = 2.5f
private val SearchFillPath = pathOf(
    "M19.7689 10.5095C19.7689 13.0664 18.7325 15.3812 17.0569 17.0569C15.3812 18.7325 13.0664 19.7689 10.5095 19.7689C5.3956 19.7689 1.25 15.6233 1.25 10.5095C1.25 5.3956 5.3956 1.25 10.5095 1.25C15.6233 1.25 19.7689 5.3956 19.7689 10.5095Z",
)
private val SearchStrokePath = pathOf(
    "M22.0838 22.0838L17.0569 17.0569M17.0569 17.0569C18.7325 15.3812 19.7689 13.0664 19.7689 10.5095C19.7689 5.3956 15.6233 1.25 10.5095 1.25C5.3956 1.25 1.25 5.3956 1.25 10.5095C1.25 15.6233 5.3956 19.7689 10.5095 19.7689C13.0664 19.7689 15.3812 18.7325 17.0569 17.0569Z",
)

private const val HeartViewportWidth = 22f
private const val HeartViewportHeight = 20f
private const val HeartStrokeWidth = 2.5f
private val HeartOutlinePath = pathOf(
    "M18.6201 10.5476L12.1489 16.8809C11.3715 17.6418 10.1285 17.6418 9.35109 16.8809L2.87994 10.5476C0.706686 8.42065 0.706686 4.97217 2.87994 2.84522C5.0532 0.718261 8.57674 0.718261 10.75 2.84522C12.9233 0.718261 16.4468 0.718261 18.6201 2.84522C20.7933 4.97217 20.7933 8.42065 18.6201 10.5476Z",
)
private val HeartSelectedPath = pathOf(
    "M18.6201 10.5476L10.75 18.25L2.87994 10.5476C0.706686 8.42065 0.706686 4.97217 2.87994 2.84522C5.0532 0.718261 8.57674 0.718261 10.75 2.84522C12.9233 0.718261 16.4468 0.718261 18.6201 2.84522C20.7933 4.97217 20.7933 8.42065 18.6201 10.5476Z",
)

private const val ProfileOutlineViewport = 21f
private const val ProfileSelectedViewport = 18f
private const val ProfileOutlineStrokeWidth = 2.5f
private const val ProfileSelectedInset = 1f
private val ProfileOutlineBodyPath = pathOf(
    "M1.25 16.5C1.25 14.2909 3.04086 12.5 5.25 12.5H15.25C17.4591 12.5 19.25 14.2909 19.25 16.5V17.25C19.25 18.3546 18.3546 19.25 17.25 19.25H3.25C2.14543 19.25 1.25 18.3546 1.25 17.25V16.5Z",
)
private val ProfileSelectedBodyPath = pathOf(
    "M0 15.25C0 13.0409 1.79086 11.25 4 11.25H14C16.2091 11.25 18 13.0409 18 15.25V16C18 17.1046 17.1046 18 16 18H2C0.89543 18 0 17.1046 0 16V15.25Z",
)
private val ProfileOutlineCircleCenter = Offset(10.25f, 4.625f)
private const val ProfileOutlineCircleRadius = 3.375f
private val ProfileSelectedCircleCenter = Offset(9f, 3.375f)
private const val ProfileSelectedCircleRadius = 3.375f

private fun pathOf(pathData: String): Path {
    return PathParser().parsePathString(pathData).toPath()
}
