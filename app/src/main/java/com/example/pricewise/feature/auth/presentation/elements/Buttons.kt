package com.example.pricewise.feature.auth.presentation.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pricewise.R

@Composable
fun AuthorisationButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    val inter = FontFamily(
        Font(R.font.inter_regular, weight = FontWeight.W400),
        Font(R.font.inter_medium, weight = FontWeight.W500),
        Font(R.font.inter_semibold, weight = FontWeight.W600),
        Font(R.font.inter_bold, weight = FontWeight.W700),
    )

    Box(
        modifier = Modifier
            .width(345.dp)
            .height(48.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        colorResource(id = R.color.orange_gradient_start),
                        colorResource(id = R.color.orange_gradient_end)
                    )
                )
            )
            .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
            .clickable(enabled = !isLoading, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.height(24.dp),
            text = text,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontFamily = inter,
            fontWeight = FontWeight(500),
            color = colorResource(R.color.white),
            textAlign = TextAlign.Justify,
            letterSpacing = 0.3.sp,
        )
    }
}

@Composable
fun VkLoginButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(345.dp)
            .height(48.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .border(
                BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(enabled = !isLoading, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_vk_logo),
                contentDescription = stringResource(R.string.vk_logo_description),
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.vk_id_login),
                color = Color.Black
            )
        }
    }
}