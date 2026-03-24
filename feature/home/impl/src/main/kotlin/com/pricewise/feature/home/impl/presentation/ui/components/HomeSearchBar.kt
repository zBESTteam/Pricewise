package com.pricewise.feature.home.impl.presentation.ui.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import com.pricewise.feature.home.impl.R
import com.pricewise.feature.home.impl.presentation.ui.HomeColors
import com.pricewise.feature.home.impl.presentation.ui.HomeDimens
import com.pricewise.feature.home.impl.presentation.ui.HomeShapes
import com.pricewise.feature.home.impl.presentation.ui.HomeTextStyles
import com.pricewise.core.ui.R as CoreUiR

@Composable
internal fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onPhotoSearchClick: () -> Unit,
    modifier: Modifier,
) {
    val photoSearchContentDescription = stringResource(R.string.home_photo_search_content_description)

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = HomeDimens.SearchFieldHeight),
        shape = HomeShapes.SearchField,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        textStyle = HomeTextStyles.SearchField,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                tint = HomeColors.SecondaryText,
            )
        },
        trailingIcon = {
            IconButton(
                onClick = onPhotoSearchClick,
                modifier = Modifier
                    .padding(end = HomeDimens.SearchTrailingIconEndPadding)
                    .semantics {
                        role = Role.Button
                        contentDescription = photoSearchContentDescription
                    },
            ) {
                Icon(
                    painter = painterResource(id = CoreUiR.drawable.ic_camera),
                    contentDescription = null,
                    tint = HomeColors.SecondaryText,
                )
            }
        },
        placeholder = {
            Text(
                text = stringResource(R.string.home_search_placeholder),
                style = HomeTextStyles.SearchField,
                color = HomeColors.SecondaryText,
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = HomeColors.PrimaryText,
            unfocusedTextColor = HomeColors.PrimaryText,
            cursorColor = HomeColors.PrimaryText,
        ),
    )
}
