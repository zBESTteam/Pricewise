package com.vkedu.pricewise.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vkedu.pricewise.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Filters(sheetState: SheetState, closeFilters: () -> Unit) {
    var isProductChosen by rememberSaveable { mutableStateOf(true) }
    var deliveryChosen by rememberSaveable { mutableStateOf(0) }
    var onlyOriginals by rememberSaveable { mutableStateOf(false) }
    var onlyNew by rememberSaveable { mutableStateOf(false) }
    var onlyBU by rememberSaveable { mutableStateOf(false) }
    var onlyMarketplaces by rememberSaveable { mutableStateOf(false) }
    var onlyOfflineShops by rememberSaveable { mutableStateOf(false) }
    val customModifier: Modifier = Modifier.offset(y = (-7).dp)
    ModalBottomSheet(
        sheetState = sheetState, onDismissRequest = { closeFilters() }, dragHandle =
            {
                BottomSheetDefaults.DragHandle(
                    modifier = customModifier,
                    color = colorResource(R.color.handle_color),
                    width = 63.dp,
                    height = 4.dp
                )
            },
        containerColor = colorResource(R.color.white),
        shape = RoundedCornerShape(
            topStart = 35.dp,
            topEnd = 35.dp
        )
    ) {
        Box(
            modifier = customModifier
                .fillMaxSize()
                .padding(horizontal = 15.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                Text(
                    modifier = Modifier
                        .width(187.dp)
                        .height(27.dp),
                    text = stringResource(R.string.filtration),
                    style = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 26.sp,
                        fontFamily = FontFamily(Font(R.font.inter_bold)),
                        fontWeight = FontWeight(700),
                        color = colorResource(R.color.mid_dark),
                        letterSpacing = 0.3.sp
                    )
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        10.dp,
                        Alignment.CenterHorizontally
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = if (isProductChosen) listOf(
                                        colorResource(R.color.start_gradient_color),
                                        colorResource(R.color.end_gradient_color)
                                    ) else listOf(
                                        colorResource(R.color.disabled_filter_button_color),
                                        colorResource(R.color.disabled_filter_button_color)
                                    )
                                ),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { isProductChosen = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier
                                .width(49.dp)
                                .height(24.dp),
                            text = stringResource(R.string.product),
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                fontFamily = FontFamily(Font(R.font.inter)),
                                fontWeight = FontWeight(600),
                                color = if (isProductChosen) colorResource(R.color.white) else colorResource(
                                    R.color.disabled_filter_button_text_color
                                ),
                                letterSpacing = 0.3.sp,
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = if (!isProductChosen) listOf(
                                        colorResource(R.color.start_gradient_color),
                                        colorResource(R.color.end_gradient_color)
                                    ) else listOf(
                                        colorResource(R.color.disabled_filter_button_color),
                                        colorResource(R.color.disabled_filter_button_color)
                                    )
                                ),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { isProductChosen = false },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            modifier = Modifier
                                .width(49.dp)
                                .height(24.dp),
                            text = stringResource(R.string.price),
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                fontFamily = FontFamily(Font(R.font.inter)),
                                fontWeight = FontWeight(600),
                                color = if (!isProductChosen) colorResource(R.color.white) else colorResource(
                                    R.color.disabled_filter_button_text_color
                                ),
                                letterSpacing = 0.3.sp,
                            )
                        )
                    }
                }
                Text(
                    text = stringResource(R.string.delieveryDate),
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(600),
                        color = colorResource(R.color.mid_dark),
                        letterSpacing = 0.3.sp,
                    )
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                ) {
                    DeliveryDateButton(
                        text = stringResource(R.string.today),
                        isSelected = deliveryChosen == 1,
                        onClick = { deliveryChosen = if (deliveryChosen == 1) 0 else 1 }
                    )
                    DeliveryDateButton(
                        text = stringResource(R.string.today_or_tomorrow),
                        isSelected = deliveryChosen == 2,
                        onClick = { deliveryChosen = if (deliveryChosen == 2) 0 else 2 }
                    )
                    DeliveryDateButton(
                        text = stringResource(R.string.week),
                        isSelected = deliveryChosen == 3,
                        onClick = { deliveryChosen = if (deliveryChosen == 3) 0 else 3 }
                    )
                    DeliveryDateButton(
                        text = stringResource(R.string.two_weeks),
                        isSelected = deliveryChosen == 4,
                        onClick = { deliveryChosen = if (deliveryChosen == 4) 0 else 4 }
                    )
                }
                Text(
                    text = "Качество товара",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(600),
                        color = colorResource(R.color.mid_dark),

                        letterSpacing = 0.3.sp,
                    )
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(13.dp, Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .background(color = colorResource(R.color.disabled_filter_button_color), shape = RoundedCornerShape(size = 14.dp))
                            .padding(start = 14.dp, top = 10.dp, end = 14.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Только оригиналы",
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                fontFamily = FontFamily(Font(R.font.inter)),
                                fontWeight = FontWeight(500),
                                color = colorResource(R.color.disabled_filter_button_text_color),

                                letterSpacing = 0.3.sp,
                            )
                        )
                        Image(
                            modifier = Modifier.clickable { onlyOriginals = !onlyOriginals },
                            painter = if (onlyOriginals) painterResource(R.drawable.switch_on) else painterResource(R.drawable.switch_off),
                            contentDescription = null
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .background(color = colorResource(R.color.disabled_filter_button_color), shape = RoundedCornerShape(size = 14.dp))
                            .padding(start = 14.dp, top = 10.dp, end = 14.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Только новые",
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                fontFamily = FontFamily(Font(R.font.inter)),
                                fontWeight = FontWeight(500),
                                color = colorResource(R.color.disabled_filter_button_text_color),

                                letterSpacing = 0.3.sp,
                            )
                        )
                        Image(
                            modifier = Modifier.clickable { onlyNew = !onlyNew },
                            painter = if (onlyNew) painterResource(R.drawable.switch_on) else painterResource(R.drawable.switch_off),
                            contentDescription = null
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .background(color = colorResource(R.color.disabled_filter_button_color), shape = RoundedCornerShape(size = 14.dp))
                            .padding(start = 14.dp, top = 10.dp, end = 14.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Только Б/У",
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                fontFamily = FontFamily(Font(R.font.inter)),
                                fontWeight = FontWeight(500),
                                color = colorResource(R.color.disabled_filter_button_text_color),

                                letterSpacing = 0.3.sp,
                            )
                        )
                        Image(
                            modifier = Modifier.clickable { onlyBU = !onlyBU },
                            painter = if (onlyBU) painterResource(R.drawable.switch_on) else painterResource(R.drawable.switch_off),
                            contentDescription = null
                        )
                    }
                }
                Text(
                    text = "Магазины",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(600),
                        color = colorResource(R.color.mid_dark),

                        letterSpacing = 0.3.sp,
                    )
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(13.dp, Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .background(color = colorResource(R.color.disabled_filter_button_color), shape = RoundedCornerShape(size = 14.dp))
                            .padding(start = 14.dp, top = 10.dp, end = 14.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Маркетплейсы",
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                fontFamily = FontFamily(Font(R.font.inter)),
                                fontWeight = FontWeight(500),
                                color = colorResource(R.color.disabled_filter_button_text_color),

                                letterSpacing = 0.3.sp,
                            )
                        )
                        Image(
                            modifier = Modifier.clickable { onlyMarketplaces = !onlyMarketplaces },
                            painter = if (onlyMarketplaces) painterResource(R.drawable.switch_on) else painterResource(R.drawable.switch_off),
                            contentDescription = null
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .background(color = colorResource(R.color.disabled_filter_button_color), shape = RoundedCornerShape(size = 14.dp))
                            .padding(start = 14.dp, top = 10.dp, end = 14.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Оффлайн магазины",
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                fontFamily = FontFamily(Font(R.font.inter)),
                                fontWeight = FontWeight(500),
                                color = colorResource(R.color.disabled_filter_button_text_color),

                                letterSpacing = 0.3.sp,
                            )
                        )
                        Image(
                            modifier = Modifier.clickable { onlyOfflineShops = !onlyOfflineShops },
                            painter = if (onlyOfflineShops) painterResource(R.drawable.switch_on) else painterResource(R.drawable.switch_off),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DeliveryDateButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(41.dp)
            .background(
                color = if (!isSelected) colorResource(R.color.disabled_filter_button_color) else colorResource(
                    R.color.mid_dark
                ),
                shape = RoundedCornerShape(size = 14.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(all = 10.dp),
            text = text,
            style = TextStyle(
                fontSize = 15.sp,
                lineHeight = 21.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(600),
                color = if (!isSelected) colorResource(R.color.disabled_filter_button_text_color) else colorResource(
                    R.color.white
                ),
                letterSpacing = 0.3.sp,
            )
        )
    }
}