package com.pricewise.feature.search.impl.presentation.ui

import LocalCustomColors
import Typography.Inter
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pricewise.core.ui.R
import com.pricewise.feature.search.impl.presentation.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sorts(
    closeSorts: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val customModifier: Modifier = Modifier.offset(y = (-7).dp)
    var chosenSort by remember {
        mutableStateOf(
            viewModel.chosenSort.value
        )
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { closeSorts() }, dragHandle =
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
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Сортировка",
                    style = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 26.sp,
                        fontFamily = Inter,
                        fontWeight = FontWeight(700),
                        color = LocalCustomColors.current.midDark,
                    )
                )
                SortDefaultButton(
                    title = Sort.BY_UP.text,
                    isSelected = chosenSort == Sort.BY_UP || chosenSort == Sort.DEFAULT,
                    onSelect = { chosenSort = Sort.BY_UP },
                    iconId = R.drawable.ic_sortbyup
                )
                SortDefaultButton(
                    title = Sort.BY_DOWN.text,
                    isSelected = chosenSort == Sort.BY_DOWN,
                    onSelect = { chosenSort = Sort.BY_DOWN },
                    iconId = R.drawable.ic_sortbydown
                )
                SortDefaultButton(
                    title = Sort.BY_RATINGS.text,
                    isSelected = chosenSort == Sort.BY_RATINGS,
                    onSelect = { chosenSort = Sort.BY_RATINGS },
                    iconId = R.drawable.ic_star
                )
                SortDefaultButton(
                    title = Sort.BY_RATINGS_QTY.text,
                    isSelected = chosenSort == Sort.BY_RATINGS_QTY,
                    onSelect = { chosenSort = Sort.BY_RATINGS_QTY },
                    iconId = R.drawable.ic_sortbyratings
                )
                Spacer(modifier = Modifier.size(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clip(shape = RoundedCornerShape(size = 14.dp))
                            .border(
                                width = 1.5.dp,
                                color = LocalCustomColors.current.midDark,
                                shape = RoundedCornerShape(size = 14.dp)
                            )
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(size = 14.dp)
                            )
                            .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
                            .weight(1f)
                            .clickable {
                                closeSorts()
                            }
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Закрыть",
                            style = TextStyle(
                                fontSize = 14.sp,
                                lineHeight = 21.sp,
                                fontFamily = Inter,
                                fontWeight = FontWeight(600),
                                color = LocalCustomColors.current.midDark,
                                letterSpacing = 0.3.sp,
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clip(shape = RoundedCornerShape(size = 14.dp))
                            .background(
                                color = LocalCustomColors.current.midDark
                            )
                            .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
                            .weight(1f)
                            .clickable {
                                viewModel.setChosenSort(chosenSort)
                                closeSorts()
                                viewModel.performSearch(viewModel.uiState.value.query)
                            }
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Готово",
                            style = TextStyle(
                                fontSize = 14.sp,
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
fun SortDefaultButton(
    iconId: Int,
    title: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(shape = RoundedCornerShape(size = 14.dp))
            .background(
                color = LocalCustomColors.current.disabledFilterButtonColor
            )
            .clickable { onSelect() }
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier
                    .size(20.dp),
                colorFilter = ColorFilter.tint(LocalCustomColors.current.iconsColor),
                painter = painterResource(iconId),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(17.dp))

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
                modifier = Modifier
                    .size(20.dp),
                colorFilter = ColorFilter.tint(LocalCustomColors.current.iconsColor),
                painter = if (isSelected) painterResource(R.drawable.sortselected)
                else painterResource(R.drawable.sortnotselected),
                contentDescription = null
            )
        }
    }
}

enum class Sort(val text: String) {
    DEFAULT("Без сортировки"),
    BY_UP("Сначала дешевые"),
    BY_DOWN("Сначала дорогие"),
    BY_RATINGS("По рейтингу"),
    BY_RATINGS_QTY("По количеству отзывов"),
}
