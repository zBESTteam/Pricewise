package com.pricewise.feature.search.impl.presentation.ui

import LocalCustomColors
import Typography.Inter
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pricewise.core.ui.R
import com.pricewise.feature.search.impl.presentation.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Filters(sheetState: SheetState, closeFilters: () -> Unit, viewModel: SearchViewModel) {
    var isProductChosen by rememberSaveable { mutableStateOf(true) }
    var deliveryChosen by rememberSaveable { mutableIntStateOf(0) }
    var onlyOriginals by rememberSaveable { mutableStateOf(false) }
    var onlyNew by rememberSaveable { mutableStateOf(false) }
    var onlyUsed by rememberSaveable { mutableStateOf(false) }
    var onlyMarketplaces by rememberSaveable { mutableStateOf(false) }
    var onlyOfflineShops by rememberSaveable { mutableStateOf(false) }
    var priceFrom by rememberSaveable { mutableLongStateOf(0L) }
    var priceTo by rememberSaveable { mutableLongStateOf(0L) }
    var popularDiapasonChosen by rememberSaveable { mutableIntStateOf(0) }
    var canPayLater by rememberSaveable { mutableStateOf(false) }
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        isProductChosen = viewModel.isProductChosen.value
        deliveryChosen = viewModel.deliveryChosen.value
        onlyOriginals = viewModel.onlyOriginals.value
        onlyNew = viewModel.onlyNew.value
        onlyUsed = viewModel.onlyUsed.value
        onlyMarketplaces = viewModel.onlyMarketplaces.value
        onlyOfflineShops = viewModel.onlyOfflineShops.value
        priceFrom = viewModel.priceFrom.value
        priceTo = viewModel.priceTo.value
        popularDiapasonChosen = viewModel.popularDiapasonChosen.value
        canPayLater = viewModel.canPayLater.value
    }
    val customModifier: Modifier = Modifier.offset(y = (-7).dp)
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { closeFilters() }, dragHandle =
            {
                BottomSheetDefaults.DragHandle(
                    modifier = customModifier,
                    color = LocalCustomColors.current.handleColor,
                    width = 63.dp,
                    height = 4.dp
                )
            },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(
            topStart = 35.dp,
            topEnd = 35.dp
        )
    ) {
        Box(
            modifier = customModifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 15.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp), modifier = Modifier
                    .wrapContentHeight()
            ) {
                Text(
                    modifier = Modifier
                        .width(187.dp)
                        .height(27.dp),
                    text = stringResource(R.string.filtration),
                    style = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 26.sp,
                        fontFamily = Inter,
                        fontWeight = FontWeight(700),
                        color = LocalCustomColors.current.midDark,
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
                                        LocalCustomColors.current.startGradient,
                                        LocalCustomColors.current.endGradient
                                    ) else listOf(
                                        LocalCustomColors.current.disabledFilterButtonColor,
                                        LocalCustomColors.current.disabledFilterButtonColor
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
                                fontFamily = Inter,
                                fontWeight = FontWeight(600),
                                color = if (isProductChosen) MaterialTheme.colorScheme.onPrimary else LocalCustomColors.current.disabledFilterButtonTextColor,
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
                                        LocalCustomColors.current.startGradient,
                                        LocalCustomColors.current.endGradient
                                    ) else listOf(
                                        LocalCustomColors.current.disabledFilterButtonColor,
                                        LocalCustomColors.current.disabledFilterButtonColor
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
                                fontFamily = Inter,
                                fontWeight = FontWeight(600),
                                color = if (!isProductChosen) MaterialTheme.colorScheme.onPrimary else LocalCustomColors.current.disabledFilterButtonTextColor,
                                letterSpacing = 0.3.sp,
                            )
                        )
                    }
                }
                if (isProductChosen) {
                    Text(
                        text = stringResource(R.string.delieveryDate),
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            fontFamily = Inter,
                            fontWeight = FontWeight(600),
                            color = LocalCustomColors.current.midDark,
                            letterSpacing = 0.3.sp,
                        )
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                    ) {
                        FilterDefaultButton(
                            text = stringResource(R.string.today),
                            isSelected = deliveryChosen == 1,
                            onClick = { deliveryChosen = if (deliveryChosen == 1) 0 else 1 }
                        )
                        FilterDefaultButton(
                            text = stringResource(R.string.today_or_tomorrow),
                            isSelected = deliveryChosen == 2,
                            onClick = { deliveryChosen = if (deliveryChosen == 2) 0 else 2 }
                        )
                        FilterDefaultButton(
                            text = stringResource(R.string.week),
                            isSelected = deliveryChosen == 3,
                            onClick = { deliveryChosen = if (deliveryChosen == 3) 0 else 3 }
                        )
                        FilterDefaultButton(
                            text = stringResource(R.string.two_weeks),
                            isSelected = deliveryChosen == 4,
                            onClick = { deliveryChosen = if (deliveryChosen == 4) 0 else 4 }
                        )
                    }
                    Text(
                        text = stringResource(R.string.quality),
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            fontFamily = Inter,
                            fontWeight = FontWeight(600),
                            color = LocalCustomColors.current.midDark,

                            letterSpacing = 0.3.sp,
                        )
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(13.dp, Alignment.Top),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        FilterSwitch(
                            title = stringResource(R.string.only_originals),
                            isChecked = onlyOriginals,
                            onCheckedChange = { onlyOriginals = it }
                        )

                        FilterSwitch(
                            title = stringResource(R.string.only_new),
                            isChecked = onlyNew,
                            onCheckedChange = { onlyNew = it }
                        )

                        FilterSwitch(
                            title = stringResource(R.string.only_bu),
                            isChecked = onlyUsed,
                            onCheckedChange = { onlyUsed = it })
                    }
                    Text(
                        text = stringResource(R.string.shops),
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            fontFamily = Inter,
                            fontWeight = FontWeight(600),
                            color = LocalCustomColors.current.midDark,

                            letterSpacing = 0.3.sp,
                        )
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(13.dp, Alignment.Top),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        FilterSwitch(
                            title = stringResource(R.string.marketplaces),
                            isChecked = onlyMarketplaces,
                            onCheckedChange = { onlyMarketplaces = it }
                        )

                        FilterSwitch(
                            title = stringResource(R.string.offline_shops),
                            isChecked = onlyOfflineShops,
                            onCheckedChange = { onlyOfflineShops = it }
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .background(
                                color = LocalCustomColors.current.midDark,
                                shape = RoundedCornerShape(size = 14.dp)
                            )
                            .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
                            .clickable {
                                viewModel.setIsProductChosen(isProductChosen)
                                viewModel.setDeliveryChosen(deliveryChosen)
                                viewModel.setOnlyOriginals(onlyOriginals)
                                viewModel.setOnlyNew(onlyNew)
                                viewModel.setOnlyUsed(onlyUsed)
                                viewModel.setOnlyMarketplaces(onlyMarketplaces)
                                viewModel.setOnlyOfflineShops(onlyOfflineShops)
                                viewModel.setPriceFrom(priceFrom)
                                viewModel.setPriceTo(priceTo)
                                viewModel.setPopularDiapasonChosen(popularDiapasonChosen)
                                viewModel.setCanPayLater(canPayLater)
                                // Добавить начало поиска заново
                                closeFilters()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.apply),
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 21.sp,
                                fontFamily = Inter,
                                fontWeight = FontWeight(600),
                                color = MaterialTheme.colorScheme.onPrimary,
                                letterSpacing = 0.3.sp,
                            )
                        )
                    }
                } else {
                    Text(
                        text = stringResource(R.string.sort_price),
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            fontFamily = Inter,
                            fontWeight = FontWeight(600),
                            color = LocalCustomColors.current.midDark,
                            letterSpacing = 0.3.sp,
                        ),
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            10.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        PriceInputField(
                            modifier = Modifier.weight(1f),
                            text = "От",
                            onValueChange = {
                                priceFrom = it
                                popularDiapasonChosen = 0
                            },
                            context = LocalContext.current,
                            value = priceFrom.toString()
                        )
                        PriceInputField(
                            modifier = Modifier.weight(1f),
                            text = "До",
                            onValueChange = {
                                priceTo = it
                                popularDiapasonChosen = 0
                            },
                            context = LocalContext.current,
                            value = priceTo.toString()
                        )
                    }
                    Box(
                        modifier = Modifier
                            .width(345.dp)
                            .height(13.9.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        val minPrice =
                            state.value.items.minByOrNull { it.price }?.price?.toFloat() ?: 0f
                        val maxPrice =
                            state.value.items.maxByOrNull { it.price }?.price?.toFloat() ?: 0f
                        val priceRange = maxPrice - minPrice
                        val normalizedFrom = if (priceRange > 0) {
                            ((priceFrom - minPrice) / priceRange).coerceIn(0f, 1f)
                        } else {
                            0f
                        }
                        val normalizedTo = if (priceRange > 0) {
                            ((priceTo - minPrice) / priceRange).coerceIn(0f, 1f)
                        } else {
                            1f
                        }

                        val containerWidth = 345.dp
                        val leftOffset = containerWidth * normalizedFrom
                        val rightOffset = containerWidth * (1 - normalizedTo)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .align(Alignment.Center)
                                .background(
                                    color = LocalCustomColors.current.disabledFilterButtonColor,
                                    shape = RoundedCornerShape(size = 14.dp)
                                )
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .align(Alignment.Center)
                                .padding(start = leftOffset, end = rightOffset)
                                .background(
                                    brush = Brush.linearGradient(
                                        listOf(
                                            LocalCustomColors.current.startGradient,
                                            LocalCustomColors.current.endGradient
                                        )
                                    ),
                                    shape = RoundedCornerShape(size = 14.dp)
                                )
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .offset(x = leftOffset)
                                .size(if (priceTo - priceFrom > 0) 14.dp else 0.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        listOf(
                                            LocalCustomColors.current.startGradient,
                                            LocalCustomColors.current.endGradient
                                        )
                                    ),
                                    shape = CircleShape
                                )
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .offset(x = (containerWidth - rightOffset) - 14.dp)
                                .size(if (priceTo - priceFrom > 0) 14.dp else 0.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        listOf(
                                            LocalCustomColors.current.startGradient,
                                            LocalCustomColors.current.endGradient
                                        )
                                    ),
                                    shape = CircleShape
                                )
                        )
                    }
                    Text(
                        text = stringResource(R.string.popular_diapasons),
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            fontFamily = Inter,
                            fontWeight = FontWeight(600),
                            color = LocalCustomColors.current.midDark,
                            letterSpacing = 0.3.sp,
                        )
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            10.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        FilterDefaultButton(
                            text = stringResource(R.string.below_80_000),
                            isSelected = popularDiapasonChosen == 1,
                            onClick = {
                                popularDiapasonChosen = if (popularDiapasonChosen == 1) 0 else 1
                                priceFrom = 0
                                priceTo = 0
                            })
                        FilterDefaultButton(
                            text = stringResource(R.string.in_80_000_120_000),
                            isSelected = popularDiapasonChosen == 2,
                            onClick = {
                                popularDiapasonChosen = if (popularDiapasonChosen == 2) 0 else 2
                                priceFrom = 0
                                priceTo = 0
                            })
                    }
                    FilterDefaultButton(
                        text = stringResource(R.string.after_120_000),
                        isSelected = popularDiapasonChosen == 3,
                        onClick = {
                            popularDiapasonChosen = if (popularDiapasonChosen == 3) 0 else 3
                            priceFrom = 0
                            priceTo = 0
                        })
                    Text(
                        text = stringResource(R.string.pay_later),
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            fontFamily = Inter,
                            fontWeight = FontWeight(600),
                            color = LocalCustomColors.current.midDark,

                            letterSpacing = 0.3.sp,
                        )
                    )
                    FilterSwitch(
                        modifier = Modifier.height(60.dp),
                        title = stringResource(R.string.show_product_pay_later),
                        isChecked = canPayLater,
                        onCheckedChange = { canPayLater = it })
                    Spacer(modifier = Modifier.size(56.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .background(
                                color = LocalCustomColors.current.midDark,
                                shape = RoundedCornerShape(size = 14.dp)
                            )
                            .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
                            .clickable {
                                viewModel.setIsProductChosen(isProductChosen)
                                viewModel.setDeliveryChosen(deliveryChosen)
                                viewModel.setOnlyOriginals(onlyOriginals)
                                viewModel.setOnlyNew(onlyNew)
                                viewModel.setOnlyUsed(onlyUsed)
                                viewModel.setOnlyMarketplaces(onlyMarketplaces)
                                viewModel.setOnlyOfflineShops(onlyOfflineShops)
                                viewModel.setPriceFrom(priceFrom)
                                viewModel.setPriceTo(priceTo)
                                viewModel.setPopularDiapasonChosen(popularDiapasonChosen)
                                viewModel.setCanPayLater(canPayLater)
                                // Добавить начало поиска заново
                                closeFilters()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.apply),
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 21.sp,
                                fontFamily = Inter,
                                fontWeight = FontWeight(600),
                                color = MaterialTheme.colorScheme.onPrimary,
                                letterSpacing = 0.3.sp,
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterDefaultButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(41.dp)
            .background(
                color = if (!isSelected) LocalCustomColors.current.disabledFilterButtonColor else LocalCustomColors.current.midDark,
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
                fontFamily = Inter,
                fontWeight = FontWeight(600),
                color = if (!isSelected) LocalCustomColors.current.disabledFilterButtonTextColor else MaterialTheme.colorScheme.onPrimary,
                letterSpacing = 0.3.sp,
            )
        )
    }
}

@Composable
fun FilterSwitch(
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(
                color = LocalCustomColors.current.disabledFilterButtonColor,
                shape = RoundedCornerShape(size = 14.dp)
            )
            .padding(horizontal = 14.dp)
            .clickable { onCheckedChange(!isChecked) },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontFamily = Inter,
                    fontWeight = FontWeight(500),
                    color = LocalCustomColors.current.disabledFilterButtonTextColor,
                    letterSpacing = 0.3.sp,
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                painter = if (isChecked) painterResource(R.drawable.switch_on)
                else painterResource(R.drawable.switch_off),
                contentDescription = null
            )
        }
    }
}

@Composable
fun PriceInputField(
    modifier: Modifier = Modifier,
    value: String,
    text: String,
    onValueChange: (Long) -> Unit,
    context: Context
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue(value)) }

    Box(
        modifier = modifier
            .height(44.dp)
            .background(
                color = LocalCustomColors.current.disabledFilterButtonColor,
                shape = RoundedCornerShape(size = 14.dp)
            )
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight(500),
                    color = LocalCustomColors.current.disabledFilterButtonTextColor,
                    letterSpacing = 0.3.sp,
                )
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                BasicTextField(
                    value = textFieldValue,
                    onValueChange = { newValue ->
                        val filteredText = newValue.text.filter { it.isDigit() }
                        val cleanedText = filteredText.trimStart('0')
                        val resultText = cleanedText.ifEmpty { "" }
                        val limitedText = if (resultText.length > 11) {
                            context.getString(R.string.long_max)
                        } else {
                            resultText
                        }
                        textFieldValue = newValue.copy(
                            text = limitedText,
                            selection = TextRange(limitedText.length)
                        )
                        try {
                            val longValue = if (limitedText.isEmpty()) 0L else limitedText.toLong()
                            onValueChange(longValue)
                        } catch (e: NumberFormatException) {
                            onValueChange(0L)
                        }
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight(500),
                        color = LocalCustomColors.current.disabledFilterButtonTextColor,
                        letterSpacing = 0.3.sp,
                        textAlign = TextAlign.Center
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = VisualTransformation.None,
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            innerTextField()
                        }
                    }
                )
            }

            Text(
                text = "₽",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight(500),
                    color = LocalCustomColors.current.disabledFilterButtonTextColor,
                    letterSpacing = 0.3.sp,
                )
            )
        }
    }
}