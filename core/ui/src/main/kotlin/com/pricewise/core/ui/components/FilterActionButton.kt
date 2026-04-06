package com.pricewise.core.ui.components

import Typography.Inter
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pricewise.core.ui.R

@Composable
fun FilterActionButton(
    text: String? = null,
    icon: Int? = null,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    var buttonModifier = Modifier
        .height(41.dp)
        .background(
            color = if (isSelected) {
                colorResource(R.color.mid_dark)
            } else {
                colorResource(R.color.disabled_filter_button_color)
            },
            shape = RoundedCornerShape(size = 14.dp),
        )
        .clickable(onClick = onClick)

    if (icon != null && text == null) {
        buttonModifier = buttonModifier.width(41.dp)
    }

    Row(
        modifier = buttonModifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        if (icon != null) {
            Icon(
                modifier = Modifier.padding(all = 10.dp),
                painter = painterResource(icon),
                tint = colorResource(R.color.icons_color),
                contentDescription = null,
            )
        }
        if (text != null) {
            Text(
                modifier = Modifier.padding(all = 10.dp),
                text = text,
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 21.sp,
                    fontFamily = Inter,
                    fontWeight = FontWeight.W600,
                    color = if (isSelected) {
                        colorResource(R.color.white)
                    } else {
                        colorResource(R.color.disabled_filter_button_text_color)
                    },
                    letterSpacing = 0.3.sp,
                ),
            )
        }
    }
}

