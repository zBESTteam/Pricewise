package com.pricewise.feature.auth.impl.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pricewise.feature.auth.impl.presentation.viewmodel.RegistrationViewModel
import com.pricewise.feature.auth.impl.R
import com.pricewise.feature.auth.impl.presentation.auth.components.AuthorisationButton
import com.pricewise.feature.auth.impl.presentation.auth.components.EmailInputField
import com.pricewise.feature.auth.impl.presentation.auth.components.PasswordInputField

@Composable
fun RegistrationScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToMain: () -> Unit,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(uiState.session) {
        if (uiState.session != null) {
            onNavigateToMain()
        }
    }

    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            Toast.makeText(context, uiState.error, Toast.LENGTH_LONG).show()
        }
    }

    val inter = remember {
        FontFamily(
            Font(R.font.inter_regular, weight = FontWeight.W400),
            Font(R.font.inter_medium, weight = FontWeight.W500),
            Font(R.font.inter_semibold, weight = FontWeight.W600),
            Font(R.font.inter_bold, weight = FontWeight.W700),
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(
                orientation = Orientation.Horizontal,
                state = rememberScrollState()
            )
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(120.dp))
                Image(
                    modifier = Modifier
                        .width(96.dp)
                        .height(96.dp),
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = stringResource(R.string.logo_description)
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = stringResource(R.string.default_register_text),
                    style = TextStyle(
                        fontSize = 24.sp,
                        lineHeight = 31.2.sp,
                        fontFamily = inter,
                        fontWeight = FontWeight(700),
                        color = colorResource(R.color.login_Pricewise_color),
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                EmailInputField(
                    email = uiState.email,
                    onValueChange = viewModel::onEmailChange
                )

                Spacer(modifier = Modifier.height(16.dp))

                var passwordVisible by remember { androidx.compose.runtime.mutableStateOf(false) }
                PasswordInputField(
                    password = uiState.password,
                    passwordVisible = passwordVisible,
                    onValueChange = viewModel::onPasswordChange,
                    changeVisibility = { passwordVisible = it },
                    stringResource(R.string.default_password_text)
                )
                Spacer(modifier = Modifier.height(16.dp))

                var confirmPasswordVisible by remember { androidx.compose.runtime.mutableStateOf(false) }
                PasswordInputField(
                    password = uiState.passwordConfirm,
                    passwordVisible = confirmPasswordVisible,
                    onValueChange = viewModel::onPasswordConfirmChange,
                    changeVisibility = { confirmPasswordVisible = it },
                    stringResource(R.string.Confirm_password_text)
                )

                Spacer(modifier = Modifier.height(24.dp))

                AuthorisationButton(
                    text = stringResource(R.string.default_register_text),
                    isLoading = uiState.isLoading,
                    onClick = viewModel::register
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.have_acc_question))
                    TextButton(onClick = { onNavigateToLogin() }, enabled = !uiState.isLoading) {
                        Text(text = stringResource(R.string.login),
                            color = colorResource(id = R.color.black))
                    }
                }
            }
            if (uiState.isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}
