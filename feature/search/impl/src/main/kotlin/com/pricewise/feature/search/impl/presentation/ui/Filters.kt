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
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToLong

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Filters(sheetState: SheetState, closeFilters: () -> Unit, viewModel: SearchViewModel) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val minPrice =
        state.value.items.minByOrNull { it.price }?.price?.toFloat() ?: 0f
    val maxPrice =
        state.value.items.maxByOrNull { it.price }?.price?.toFloat() ?: 0f
    var currentFiltersState by remember {
        mutableStateOf(
            FiltersState(
                priceFrom = floor(minPrice).toLong(),
                priceTo = ceil(maxPrice).toLong()
            )
        )
    }
    var isProductChosen by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        isProductChosen = viewModel.isProductChosen.value
        currentFiltersState = viewModel.filtersState.value
    }
    val customModifier: Modifier = Modifier.offset(y = (-7).dp)

    fun apply() {
        viewModel.setIsProductChosen(isProductChosen)
        viewModel.setDeliveryChosen(currentFiltersState.deliveryChosen)
        viewModel.setOnlyOriginals(currentFiltersState.onlyOriginals)
        viewModel.setOnlyNew(currentFiltersState.onlyNew)
        viewModel.setOnlyUsed(currentFiltersState.onlyUsed)
        viewModel.setOnlyMarketplaces(currentFiltersState.onlyMarketplaces)
        viewModel.setOnlyOfflineShops(currentFiltersState.onlyOfflineShops)
        viewModel.setPriceFrom(currentFiltersState.priceFrom)
        viewModel.setPriceTo(currentFiltersState.priceTo)
        viewModel.setPopularDiapasonChosen(currentFiltersState.popularDiapasonChosen)
        viewModel.setCanPayLater(currentFiltersState.canPayLater)
        viewModel.performSearch(viewModel.uiState.value.query) // Должно начать поиск с фильтрами (не проверено)
        closeFilters()
    }


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
                            isSelected = currentFiltersState.deliveryChosen == Delivery.TODAY,
                            onClick = {
                                currentFiltersState =
                                    currentFiltersState.copy(deliveryChosen = if (currentFiltersState.deliveryChosen == Delivery.TODAY) Delivery.NONE else Delivery.TODAY)
                            }
                        )
                        FilterDefaultButton(
                            text = stringResource(R.string.today_or_tomorrow),
                            isSelected = currentFiltersState.deliveryChosen == Delivery.TODAY_OR_TOMORROW,
                            onClick = {
                                currentFiltersState =
                                    currentFiltersState.copy(deliveryChosen = if (currentFiltersState.deliveryChosen == Delivery.TODAY_OR_TOMORROW) Delivery.NONE else Delivery.TODAY_OR_TOMORROW)
                            }
                        )
                        FilterDefaultButton(
                            text = stringResource(R.string.week),
                            isSelected = currentFiltersState.deliveryChosen == Delivery.WEEK,
                            onClick = {
                                currentFiltersState =
                                    currentFiltersState.copy(deliveryChosen = if (currentFiltersState.deliveryChosen == Delivery.WEEK) Delivery.NONE else Delivery.WEEK)
                            }
                        )
                        FilterDefaultButton(
                            text = stringResource(R.string.two_weeks),
                            isSelected = currentFiltersState.deliveryChosen == Delivery.TWO_WEEKS,
                            onClick = {
                                currentFiltersState =
                                    currentFiltersState.copy(deliveryChosen = if (currentFiltersState.deliveryChosen == Delivery.TWO_WEEKS) Delivery.NONE else Delivery.TWO_WEEKS)
                            }
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
                            isChecked = currentFiltersState.onlyOriginals,
                            onCheckedChange = {
                                currentFiltersState = currentFiltersState.copy(onlyOriginals = it)
                            }
                        )

                        FilterSwitch(
                            title = stringResource(R.string.only_new),
                            isChecked = currentFiltersState.onlyNew,
                            onCheckedChange = {
                                currentFiltersState = currentFiltersState.copy(onlyNew = it)
                            }
                        )

                        FilterSwitch(
                            title = stringResource(R.string.only_bu),
                            isChecked = currentFiltersState.onlyUsed,
                            onCheckedChange = {
                                currentFiltersState = currentFiltersState.copy(onlyUsed = it)
                            })
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
                            isChecked = currentFiltersState.onlyMarketplaces,
                            onCheckedChange = {
                                currentFiltersState =
                                    currentFiltersState.copy(onlyMarketplaces = it)
                            }
                        )

                        FilterSwitch(
                            title = stringResource(R.string.offline_shops),
                            isChecked = currentFiltersState.onlyOfflineShops,
                            onCheckedChange = {
                                currentFiltersState =
                                    currentFiltersState.copy(onlyOfflineShops = it)
                            }
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
                                apply()
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
                                currentFiltersState = currentFiltersState.copy(priceFrom = it)
                                currentFiltersState =
                                    currentFiltersState.copy(popularDiapasonChosen = 0)
                            },
                            context = LocalContext.current,
                            value = currentFiltersState.priceFrom.toString()
                        )
                        PriceInputField(
                            modifier = Modifier.weight(1f),
                            text = "До",
                            onValueChange = {
                                currentFiltersState = currentFiltersState.copy(priceTo = it)
                                currentFiltersState =
                                    currentFiltersState.copy(popularDiapasonChosen = 0)
                            },
                            context = LocalContext.current,
                            value = currentFiltersState.priceTo.toString()
                        )
                    }
                    Box(
                        modifier = Modifier
                            .width(345.dp)
                            .height(13.9.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        val priceRange = maxPrice - minPrice
                        val normalizedFrom = if (priceRange > 0) {
                            ((currentFiltersState.priceFrom - minPrice) / priceRange).coerceIn(
                                0f,
                                1f
                            )
                        } else {
                            0f
                        }
                        val normalizedTo = if (priceRange > 0) {
                            ((currentFiltersState.priceTo - minPrice) / priceRange).coerceIn(0f, 1f)
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
                                .size(if (currentFiltersState.priceTo - currentFiltersState.priceFrom > 0) 14.dp else 0.dp)
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
                                .size(if (currentFiltersState.priceTo - currentFiltersState.priceFrom > 0) 14.dp else 0.dp)
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
                            text = "До ${(minPrice + (maxPrice - minPrice) / 4).toRubles()}",
                            isSelected = currentFiltersState.popularDiapasonChosen == 1,
                            onClick = {
                                currentFiltersState =
                                    currentFiltersState.copy(popularDiapasonChosen = if (currentFiltersState.popularDiapasonChosen == 1) 0 else 1)
                                currentFiltersState =
                                    currentFiltersState.copy(priceFrom = 0)
                                currentFiltersState =
                                    currentFiltersState.copy(priceTo = (minPrice + (maxPrice - minPrice) / 4).toLong())
                            })
                        FilterDefaultButton(
                            text = "${(minPrice + (maxPrice - minPrice) / 4).toRubles()} - ${(minPrice + (maxPrice - minPrice) * 0.75f).toRubles()}",
                            isSelected = currentFiltersState.popularDiapasonChosen == 2,
                            onClick = {
                                currentFiltersState =
                                    currentFiltersState.copy(popularDiapasonChosen = if (currentFiltersState.popularDiapasonChosen == 2) 0 else 2)
                                currentFiltersState =
                                    currentFiltersState.copy(priceFrom = (minPrice + (maxPrice - minPrice) / 4).toLong())
                                currentFiltersState =
                                    currentFiltersState.copy(priceTo = (minPrice + (maxPrice - minPrice) * 0.75f).toLong())
                            })
                    }
                    FilterDefaultButton(
                        text = "${(minPrice + (maxPrice - minPrice) * 0.75f).toRubles()} и дороже",
                        isSelected = currentFiltersState.popularDiapasonChosen == 3,
                        onClick = {
                            currentFiltersState =
                                currentFiltersState.copy(popularDiapasonChosen = if (currentFiltersState.popularDiapasonChosen == 3) 0 else 3)
                            currentFiltersState =
                                currentFiltersState.copy(priceFrom = (minPrice + (maxPrice - minPrice) * 0.75f).toLong())
                            currentFiltersState =
                                currentFiltersState.copy(priceTo = Long.MAX_VALUE)
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
                        isChecked = currentFiltersState.canPayLater,
                        onCheckedChange = {
                            currentFiltersState = currentFiltersState.copy(canPayLater = it)
                        })
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
                                apply()
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
    LaunchedEffect(value) {
        if (textFieldValue.text != value) {
            textFieldValue = TextFieldValue(value, selection = TextRange(value.length))
        }
        if (textFieldValue.text == Long.MAX_VALUE.toString()) textFieldValue = TextFieldValue("-")
    }
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

private fun Float.toRubles(): String {
    return "%,d ₽".format(this.toLong()).replace(',', ' ')
}