package com.example.pricewise.feature.favorites.domain.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pricewise.R

@Composable
fun FavoritesScreen() {
    val inter = FontFamily(
        Font(R.font.inter_regular, weight = FontWeight.W400),
        Font(R.font.inter_medium, weight = FontWeight.W500),
        Font(R.font.inter_semibold, weight = FontWeight.W600),
        Font(R.font.inter_bold, weight = FontWeight.W700),
    )
    Column(modifier = Modifier.fillMaxSize()) {
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
                .height(44.dp)
                .background(brush = gradient, shape = heroShape)
        )

        Text( modifier = Modifier.padding(start = 15.dp, top = 14.dp),
            text = "Избранное",
            style = TextStyle(
                fontSize = 24.sp,
                lineHeight = 31.2.sp,
                fontFamily = inter,
                fontWeight = FontWeight(700),
                color = colorResource(R.color.mid_dark),
            )
        )
    }
}