package com.pricewise.core.ui.components

import Typography.Inter
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import com.pricewise.core.ui.R

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onPhotoSearchClick: () -> Unit,
    modifier: Modifier,
) {
    val photoSearchContentDescription = stringResource(R.string.search_action_content_description)

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = PriceWiseSearchBarTokens.MinHeight),
        shape = PriceWiseSearchBarTokens.Shape,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch() },
        ),
        textStyle = PriceWiseSearchBarTokens.InputTextStyle,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                tint = PriceWiseSearchBarTokens.IconTint,
            )
        },
        trailingIcon = {
            IconButton(
                onClick = onPhotoSearchClick,
                modifier = Modifier
                    .padding(end = PriceWiseSearchBarTokens.TrailingIconEndPadding)
                    .semantics {
                        role = Role.Button
                        contentDescription = photoSearchContentDescription
                    },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_camera),
                    contentDescription = null,
                    tint = PriceWiseSearchBarTokens.IconTint,
                )
            }
        },
        placeholder = {
            Text(
                text = stringResource(R.string.search_hint),
                style = PriceWiseSearchBarTokens.PlaceholderTextStyle,
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = PriceWiseSearchBarTokens.ContainerColor,
            unfocusedContainerColor = PriceWiseSearchBarTokens.ContainerColor,
            focusedBorderColor = PriceWiseSearchBarTokens.BorderColor,
            unfocusedBorderColor = PriceWiseSearchBarTokens.BorderColor,
            focusedTextColor = PriceWiseSearchBarTokens.InputTextColor,
            unfocusedTextColor = PriceWiseSearchBarTokens.InputTextColor,
            cursorColor = PriceWiseSearchBarTokens.InputTextColor,
        ),
    )
}
