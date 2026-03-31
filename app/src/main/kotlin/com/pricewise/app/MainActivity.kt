package com.pricewise.app

import com.pricewise.feature.auth.impl.presentation.auth.AuthorizationScreen
import com.pricewise.feature.auth.impl.presentation.auth.RegistrationScreen
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Временное решение для навигации без NavHost
            var isRegisterScreen by remember { mutableStateOf(false) }

            if (isRegisterScreen) {
                RegistrationScreen(
                    onNavigateToLogin = { isRegisterScreen = false },
                    onNavigateToMain = {
                        Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                AuthorizationScreen(
                    onNavigateToRegistration = { isRegisterScreen = true },
                    onNavigateToMain = {
                        Toast.makeText(this, "Вход выполнен!", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }