package com.example.pricewise.feature.main.presentation

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.pricewise.R

object MainTypography {
    val Inter = FontFamily(
        Font(R.font.inter_regular, weight = FontWeight.W400),
        Font(R.font.inter_medium, weight = FontWeight.W500),
        Font(R.font.inter_semibold, weight = FontWeight.W600),
        Font(R.font.inter_bold, weight = FontWeight.W700),
    )

    val Search = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontFamily = Inter,
        fontWeight = FontWeight.W500,
    )

    val SectionTitle = TextStyle(
        fontSize = 20.sp,
        lineHeight = 26.sp,
        fontFamily = Inter,
        fontWeight = FontWeight.W700,
    )

    val ChipText = TextStyle(
        fontSize = 14.sp,
        lineHeight = 21.sp,
        fontFamily = Inter,
        fontWeight = FontWeight.W600,
    )

    val MerchantName = TextStyle(
        fontSize = 14.sp,
        lineHeight = 21.sp,
        fontFamily = Inter,
        fontWeight = FontWeight.W600,
    )

    val ProductTitle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 18.sp,
        fontFamily = Inter,
        fontWeight = FontWeight.W400,
        letterSpacing = 0.3.sp,
    )

    val Price = TextStyle(
        fontSize = 14.sp,
        lineHeight = 21.sp,
        fontFamily = Inter,
        fontWeight = FontWeight.W700,
    )
}
