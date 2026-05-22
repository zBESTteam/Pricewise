package com.pricewise.feature.profile.impl

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

private val AppBackground = Color(0xFFF7F7F7)
private val GradientStart = Color(0xFFFF9432)
private val GradientEnd = Color(0xFFFF3426)
private val OuterAvatarStroke = Color(0xFFFB9833)
private val FieldBackground = Color(0xFFF1F1F1)
private val FieldBorder = Color(0xFFB8B8B8)
private val PrimaryText = Color(0xFF2E2E2E)
private val SecondaryText = Color(0xFF8E8E93)
private val White = Color(0xFFFFFFFF)
private val AppFont = FontFamily.SansSerif

@Composable
fun ProfileScreenRoot(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var profile by remember { mutableStateOf(viewModel.loadProfile()) }

    var showEditScreen by remember { mutableStateOf(false) }

    if (showEditScreen) {
        EditProfileScreen(
            initialProfile = profile,
            onBack = { showEditScreen = false },
            onSave = { updatedProfile ->
                profile = updatedProfile
                viewModel.saveProfile(updatedProfile)
                Toast.makeText(context, "Изменения сохранены", Toast.LENGTH_SHORT).show()
                showEditScreen = false
            }
        )
    } else {
        ProfileScreen(
            profile = profile,
            onEditProfileClick = { showEditScreen = true }
        )
    }
}

@Composable
fun ProfileScreen(
    profile: ProfileData,
    onEditProfileClick: () -> Unit
) {
    val statusTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(248.dp + statusTop)
        ) {
            ProfileHeaderBackground(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp + statusTop)
            )

            ProfileAvatar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 10.dp),
                size = 136.dp,
                showCamera = false
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "${profile.lastName} ${profile.firstName}",
                color = PrimaryText,
                fontFamily = AppFont,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ProfileMenuButton(
                    icon = Icons.Outlined.Edit,
                    text = "Редактировать профиль",
                    onClick = onEditProfileClick
                )

                ProfileMenuButton(
                    icon = Icons.Outlined.LocationOn,
                    text = "Регион",
                    value = profile.city,
                    onClick = onEditProfileClick
                )

                ProfileMenuButton(
                    icon = Icons.Outlined.WbSunny,
                    text = "Тема",
                    onClick = {}
                )

                ProfileMenuButton(
                    icon = Icons.Outlined.Chat,
                    text = "Обратиться в поддержку",
                    onClick = {}
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Выйти из аккаунта?",
            color = Color(0xFFB4B4BA),
            fontFamily = AppFont,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 20.dp)
        )
    }
}

@Composable
fun EditProfileScreen(
    initialProfile: ProfileData,
    onBack: () -> Unit,
    onSave: (ProfileData) -> Unit
) {
    var lastName by rememberSaveable { mutableStateOf(initialProfile.lastName) }
    var firstName by rememberSaveable { mutableStateOf(initialProfile.firstName) }
    var city by rememberSaveable { mutableStateOf(initialProfile.city) }
    var oldPassword by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .windowInsetsPadding(WindowInsets.statusBars)
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackGradientButton(onClick = onBack)

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = "Редактировать профиль",
                color = PrimaryText,
                fontFamily = AppFont,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 17.sp
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            ProfileAvatar(
                size = 144.dp,
                showCamera = true
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = "Фамилия",
            color = PrimaryText,
            fontFamily = AppFont,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        ProfileTextField(
            value = lastName,
            onValueChange = { lastName = it },
            placeholder = "Фамилия"
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Имя",
            color = PrimaryText,
            fontFamily = AppFont,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        ProfileTextField(
            value = firstName,
            onValueChange = { firstName = it },
            placeholder = "Имя"
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Город",
            color = PrimaryText,
            fontFamily = AppFont,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        ProfileTextField(
            value = city,
            onValueChange = { city = it },
            placeholder = "Город"
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Пароль",
            color = PrimaryText,
            fontFamily = AppFont,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        PasswordField(
            value = oldPassword,
            onValueChange = { oldPassword = it },
            placeholder = "Старый пароль"
        )

        Spacer(modifier = Modifier.height(10.dp))

        PasswordField(
            value = newPassword,
            onValueChange = { newPassword = it },
            placeholder = "Новый пароль"
        )

        Spacer(modifier = Modifier.height(10.dp))

        PasswordField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = "Подтвердите новый пароль"
        )

        Spacer(modifier = Modifier.height(26.dp))

        GradientSaveButton(
            text = "Сохранить",
            onClick = {
                onSave(
                    ProfileData(
                        firstName = firstName.trim().ifEmpty { initialProfile.firstName },
                        lastName = lastName.trim().ifEmpty { initialProfile.lastName },
                        city = city.trim().ifEmpty { initialProfile.city }
                    )
                )
            }
        )

        Spacer(modifier = Modifier.height(18.dp))
    }
}

@Composable
fun ProfileHeaderBackground(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
    ) {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height * 0.72f)
            cubicTo(
                size.width * 0.82f,
                size.height * 0.77f,
                size.width * 0.74f,
                size.height * 0.46f,
                size.width * 0.50f,
                size.height * 0.49f
            )
            cubicTo(
                size.width * 0.26f,
                size.height * 0.46f,
                size.width * 0.18f,
                size.height * 0.77f,
                0f,
                size.height * 0.72f
            )
            close()
        }

        drawPath(
            path = path,
            brush = Brush.linearGradient(
                colors = listOf(GradientStart, GradientEnd),
                start = Offset.Zero,
                end = Offset(size.width, size.height)
            )
        )
    }
}

@Composable
fun ProfileAvatar(
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp,
    showCamera: Boolean
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(3.5.dp, OuterAvatarStroke, CircleShape)
                .padding(5.dp)
                .background(White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color(0xFFE9E9E9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = Color(0xFFC5C5C8),
                    modifier = Modifier.size(size * 0.44f)
                )
            }
        }

        if (showCamera) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-2).dp, y = (-2).dp)
                    .size(38.dp)
                    .background(White, CircleShape)
                    .border(2.dp, GradientEnd, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.CameraAlt,
                    contentDescription = null,
                    tint = GradientStart,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ProfileMenuButton(
    icon: ImageVector,
    text: String,
    value: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(White)
            .border(1.dp, FieldBorder, RoundedCornerShape(15.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = SecondaryText,
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = text,
            color = SecondaryText,
            fontFamily = AppFont,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )

        if (value != null) {
            Text(
                text = value,
                color = SecondaryText,
                fontFamily = AppFont,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = TextStyle(
            color = SecondaryText,
            fontFamily = AppFont,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        ),
        placeholder = {
            Text(
                text = placeholder,
                color = Color(0xFFB0B0B4),
                fontFamily = AppFont,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        },
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 58.dp),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = FieldBackground,
            unfocusedContainerColor = FieldBackground,
            disabledContainerColor = FieldBackground,
            focusedBorderColor = Color(0xFFD7D7DA),
            unfocusedBorderColor = Color(0xFFD7D7DA),
            cursorColor = GradientEnd
        )
    )
}

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    var visible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        textStyle = TextStyle(
            color = SecondaryText,
            fontFamily = AppFont,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        ),
        placeholder = {
            Text(
                text = placeholder,
                color = Color(0xFFB0B0B4),
                fontFamily = AppFont,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = null,
                tint = Color(0xFFA7A7AC)
            )
        },
        trailingIcon = {
            IconButton(onClick = { visible = !visible }) {
                Icon(
                    imageVector = if (visible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                    contentDescription = null,
                    tint = Color(0xFFA7A7AC),
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 58.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = FieldBackground,
            unfocusedContainerColor = FieldBackground,
            disabledContainerColor = FieldBackground,
            focusedBorderColor = Color(0xFFD7D7DA),
            unfocusedBorderColor = Color(0xFFD7D7DA),
            cursorColor = GradientEnd
        )
    )
}

@Composable
fun GradientSaveButton(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(9.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(GradientStart, GradientEnd),
                    start = Offset.Zero,
                    end = Offset(1200f, 1200f)
                )
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = White,
            fontFamily = AppFont,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}

@Composable
fun BackGradientButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(GradientStart, GradientEnd),
                    start = Offset.Zero,
                    end = Offset(300f, 300f)
                )
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBackIosNew,
            contentDescription = null,
            tint = White,
            modifier = Modifier.size(16.dp)
        )
    }
}
