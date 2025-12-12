package com.vkedu.pricewise.elements

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vkedu.pricewise.R
import com.vkedu.pricewise.data.FirebaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AuthorisationButton(password: String, confirmPassword: String, email: String, scope : CoroutineScope, context : Context, repository : FirebaseRepository) {
    var isLoading = false
    Button(
        onClick = {
            if (password != confirmPassword) {
                Toast.makeText(context, "Пароли не совпадают", Toast.LENGTH_SHORT)
                    .show()
                return@Button
            }
            scope.launch {
                isLoading = true
                try {
                    repository.signUp(email, password)
                    // TODO: Navigate to main screen on success
                    Toast.makeText(context, "Регистрация успешна", Toast.LENGTH_SHORT)
                        .show()
                } catch (e: Exception) {
                    Log.d("RegistrationScreen", e.message.toString())
                    Toast.makeText(
                        context,
                        "Ошибка регистрации: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                isLoading = false
            }
        },
        Modifier
            .width(345.dp)
            .height(48.dp)
            .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
        enabled = !isLoading
    ) {
        Text(modifier = Modifier
            .width(173.dp)
            .height(24.dp),
            text = stringResource(R.string.default_register_text), color = Color.White)
    }
}