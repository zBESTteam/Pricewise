package com.vkedu.pricewise.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vkedu.pricewise.R
import com.vkedu.pricewise.data.FirebaseRepository
import com.vkedu.pricewise.elements.EmailInputField
import com.vkedu.pricewise.elements.PasswordInputField
import kotlinx.coroutines.launch

@Composable
fun AuthorizationScreen() {
    val repository = remember { FirebaseRepository() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

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
                Image(
                    modifier = Modifier
                        .width(96.dp)
                        .height(96.dp),
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "PriceWise Logo"
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Вход PriceWise", fontSize = 24.sp, fontWeight = FontWeight.Bold)

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

                Button(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            try {
                                repository.signIn(email, password)
                                // TODO: Navigate to main screen on success
                                Toast.makeText(context, "Вход выполнен", Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Ошибка входа: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                            isLoading = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFD8401)),
                    enabled = !isLoading
                ) {
                    Text("Войти", color = Color.White)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("или войти с помощью")

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { /* TODO: Implement VK Login */ },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "VK Logo",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Вход по VK ID", color = Color.Black)
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Нет аккаунта?")
                    TextButton(
                        onClick = {
                        scope.launch {
                            isLoading = true
                            try {
                                repository.signUp(email, password)
                                // TODO: Navigate to main screen on success
                                Toast.makeText(context, "Регистрация успешна", Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Ошибка регистрации: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                            isLoading = false
                        }
                    }, 
                        enabled = !isLoading
                    ) {
                        Text("Зарегистрируйтесь")
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
fun AuthorizationScreenPreview() = AuthorizationScreen()
