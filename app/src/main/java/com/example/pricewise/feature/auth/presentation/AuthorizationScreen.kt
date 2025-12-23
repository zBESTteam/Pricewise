package com.example.pricewise.feature.auth.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pricewise.R
import com.example.pricewise.feature.auth.data.ApiAuthRepository
import com.example.pricewise.feature.auth.domain.model.LoginInput
import com.example.pricewise.feature.auth.presentation.elements.AuthorisationButton
import com.example.pricewise.feature.auth.presentation.elements.EmailInputField
import com.example.pricewise.feature.auth.presentation.elements.PasswordInputField
import com.example.pricewise.feature.auth.presentation.elements.VkLoginButton
import kotlinx.coroutines.launch

@Composable
fun AuthorizationScreen(
    onNavigateToRegistration: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    val repository = remember { ApiAuthRepository() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val inter = FontFamily(
        Font(R.font.inter_regular, weight = FontWeight.W400),
        Font(R.font.inter_medium, weight = FontWeight.W500),
        Font(R.font.inter_semibold, weight = FontWeight.W600),
        Font(R.font.inter_bold, weight = FontWeight.W700),
    )

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
                    text = stringResource(R.string.pricewise_login),
                    style = TextStyle(
                        fontSize = 24.sp,
                        lineHeight = 31.2.sp,
                        fontFamily = inter,
                        fontWeight = FontWeight(700),
                        color = colorResource(R.color.login_Pricewise_color),
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                EmailInputField(email = email, onValueChange = { email = it })

                Spacer(modifier = Modifier.height(16.dp))

                PasswordInputField(
                    password = password,
                    passwordVisible = passwordVisible,
                    onValueChange = { password = it },
                    changeVisibility = { passwordVisible = it },
                    stringResource(R.string.default_password_text)
                )

                Spacer(modifier = Modifier.height(24.dp))

                AuthorisationButton(
                    text = stringResource(R.string.login),
                    isLoading = isLoading,
                    onClick = {
                        scope.launch {
                            isLoading = true
                            try {
                                repository.signIn(LoginInput(email, password))
                                Toast.makeText(context, "Вход выполнен", Toast.LENGTH_SHORT).show()
                                onNavigateToMain()
                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    "Ошибка входа: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            isLoading = false
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(stringResource(R.string.or_login_with))

                Spacer(modifier = Modifier.height(18.dp))

                VkLoginButton(
                    isLoading = isLoading,
                    onClick = { /* TODO: Implement VK Login */ }
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(R.string.no_account_question))
                    TextButton(
                        onClick = onNavigateToRegistration,
                        enabled = !isLoading
                    ) {
                        Text(
                            text = stringResource(R.string.register_suggestion),
                            color = colorResource(id = R.color.black)
                        )
                    }
                }
            }
            if (isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AuthorizationScreenPreview() {
    AuthorizationScreen(onNavigateToRegistration = {}, onNavigateToMain = {})
}