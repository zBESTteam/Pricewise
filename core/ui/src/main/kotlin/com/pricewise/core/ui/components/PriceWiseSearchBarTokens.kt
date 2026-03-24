package com.pricewise.core.ui.components

import Typography.Inter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object PriceWiseSearchBarTokens {
    val MinHeight = 48.dp
    val Shape = RoundedCornerShape(14.dp)
    val TrailingIconEndPadding = 11.dp
    val ContainerColor = Color.White
    val BorderColor = Color.Transparent
    val InputTextColor = Color(0xFF8D9094)
    val IconTint = Color(0xFF8D9094)
    val InputTextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontFamily = Inter,
        fontWeight = FontWeight.W500,
        color = InputTextColor,
    )
    val PlaceholderTextStyle = InputTextStyle
}
