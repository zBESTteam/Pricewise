package com.vkedu.pricewise.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vkedu.pricewise.R

@Composable
fun EmailInputField(email: String, onValueChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = colorResource(R.color.input_field_border_color),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(0.5.dp)
            .width(345.dp)
            .height(48.dp)
            .background(
                color = colorResource(R.color.input_field_background_color),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier
                    .border(
                        width = 1.5.dp,
                        color = colorResource(R.color.input_field_background_color)
                    )
                    .padding(1.5.dp)
                    .width(19.5.dp)
                    .height(19.5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_mail),
                    contentDescription = "icon mail",
                    contentScale = ContentScale.None
                )
            }
            BasicTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight(500),
                    color = colorResource(R.color.input_field_text_color),
                    textAlign = TextAlign.Justify,
                    letterSpacing = 0.3.sp,
                )
            ) { innerTextField ->
                innerTextField()
                if (email.isEmpty())
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.default_mail_text),
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight(500),
                            color = colorResource(R.color.input_field_text_color),
                            textAlign = TextAlign.Justify,
                            letterSpacing = 0.3.sp,
                        )
                    )
            }
        }
    }
}

@Composable
@Preview
fun EmailInputFieldPreview() {
    EmailInputField(email = "Почта", onValueChange = {})
}


@Composable
fun PasswordInputField(
    password: String,
    passwordVisible: Boolean,
    onValueChange: (String) -> Unit,
    changeVisibility: (Boolean) -> Unit,
    default_text: String
) {
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = colorResource(R.color.input_field_border_color),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(0.5.dp)
            .width(345.dp)
            .height(48.dp)
            .background(
                color = colorResource(R.color.input_field_background_color),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier
                    .border(
                        width = 1.5.dp,
                        color = colorResource(R.color.input_field_background_color)
                    )
                    .padding(1.5.dp)
                    .width(19.5.dp)
                    .height(21.5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_lock),
                    contentDescription = "icon lock",
                    contentScale = ContentScale.None
                )
            }
            BasicTextField(
                modifier = Modifier.weight(1f),
                value = password,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight(500),
                    color = colorResource(R.color.input_field_text_color),
                    textAlign = TextAlign.Justify,
                    letterSpacing = 0.3.sp,
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(
                    '*'
                )
            ) { innerTextField ->
                innerTextField()
                if (password.isEmpty())
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = default_text,
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight(500),
                            color = colorResource(R.color.input_field_text_color),
                            textAlign = TextAlign.Justify,
                            letterSpacing = 0.3.sp,
                        )
                    )
            }
            Box(
                Modifier
                    .border(
                        width = 1.5.dp,
                        color = colorResource(R.color.input_field_background_color)
                    )
                    .padding(1.5.dp)
                    .width(21.5.dp)
                    .height(19.5.dp)
            ) {
                Image(
                    modifier = Modifier.clickable { changeVisibility(!passwordVisible) },
                    painter = if (!passwordVisible) painterResource(id = R.drawable.ic_visibility) else painterResource(
                        id = R.drawable.ic_visibility_off
                    ),
                    contentDescription = stringResource(R.string.lock_icon_description),
                    contentScale = ContentScale.None
                )
            }
        }
    }
}

@Composable
@Preview
fun PasswordInputFieldPreview() {
    PasswordInputField(
        password = "",
        passwordVisible = true,
        onValueChange = {},
        changeVisibility = { },
        default_text = "Password"
    )
}