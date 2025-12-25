package com.example.pricewise.feature.favorites.presentation.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pricewise.R

val inter = FontFamily(
    Font(R.font.inter_regular, weight = FontWeight.W400),
    Font(R.font.inter_medium, weight = FontWeight.W500),
    Font(R.font.inter_semibold, weight = FontWeight.W600),
    Font(R.font.inter_bold, weight = FontWeight.W700),
)

@Composable
fun DefaultButton(
    text: String? = null,
    icon: Int? = null,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var modifier = Modifier
        .height(41.dp)
        .background(
            color = if (!isSelected) colorResource(R.color.disabled_filter_button_color) else colorResource(
                R.color.mid_dark
            ),
            shape = RoundedCornerShape(size = 14.dp)
        )
        .clickable { onClick() }
    if (icon != null && text == null) modifier = modifier.width(41.dp)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        if (icon != null)
            Icon(
                modifier = Modifier.padding(all = 10.dp),
                painter = painterResource(icon),
                tint = colorResource(R.color.icons_color),
                contentDescription = null
            )
        if (text != null)
            Text(
                modifier = Modifier.padding(all = 10.dp),
                text = text,
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 21.sp,
                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                    fontWeight = FontWeight(600),
                    color = if (!isSelected) colorResource(R.color.disabled_filter_button_text_color) else colorResource(
                        R.color.white
                    ),
                    letterSpacing = 0.3.sp,
                )
            )
    }
}