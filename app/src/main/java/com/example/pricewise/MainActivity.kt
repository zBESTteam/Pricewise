package com.example.pricewise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pricewise.core.network.TokenStorage
import com.example.pricewise.ui.theme.PricewiseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Инициализируем хранилище токенов
        TokenStorage.init(applicationContext)
        
        enableEdgeToEdge()
        setContent {
            PricewiseTheme {
                PricewiseApp()
            }
        }
    }
}