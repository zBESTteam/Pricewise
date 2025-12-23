package com.example.pricewise.feature.auth.data

import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pricewise.R
import kotlinx.coroutines.launch
//
//@Composable
//fun RegistrationScreen() {
//    val repository = remember { /*TODO: Initialize Repository*/ }
//    val scope = rememberCoroutineScope()
//    val context = LocalContext.current
//
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
//    var passwordVisible by remember { mutableStateOf(false) }
//    var confirmPasswordVisible by remember { mutableStateOf(false) }
//    var isLoading by remember { mutableStateOf(false) }
//
//    Scaffold(
//        modifier = Modifier
//            .fillMaxSize()
//            .scrollable(
//                orientation = Orientation.Horizontal,
//                state = rememberScrollState()
//            )
//    ) { innerPadding ->
//        Box(
//            modifier = Modifier
//                .padding(innerPadding)
//                .fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(horizontal = 16.dp),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Image(
//                    modifier = Modifier
//                        .width(96.dp)
//                        .height(96.dp),
//                    painter = painterResource(id = R.drawable.ic_logo),
//                    contentDescription = stringResource(R.string.logo_description)
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Text(
//                    text = stringResource(R.string.default_register_text),
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold
//                )
//
//                Spacer(modifier = Modifier.height(32.dp))
//
//                EmailInputField(email = email, onValueChange = { email = it })
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                PasswordInputField(
//                    password = password,
//                    passwordVisible = passwordVisible,
//                    onValueChange = { password = it },
//                    changeVisibility = { passwordVisible = it },
//                    stringResource(R.string.default_password_text)
//                )
//                Spacer(modifier = Modifier.height(16.dp))
//
//                PasswordInputField(
//                    password = confirmPassword,
//                    passwordVisible = confirmPasswordVisible,
//                    onValueChange = { confirmPassword = it },
//                    changeVisibility = { confirmPasswordVisible = it },
//                    stringResource(R.string.Confirm_password_text)
//                )
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                AuthorisationButton(password, confirmPassword, email, scope, context, repository)
//
//                Spacer(modifier = Modifier.weight(1f))
//
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Text(stringResource(R.string.have_acc_question))
//                    TextButton(onClick = { /* TODO: Navigate to Login */ }, enabled = !isLoading) {
//                        Text(stringResource(R.string.login))
//                    }
//                }
//            }
//            if (isLoading) {
//                CircularProgressIndicator()
//            }
//        }
//    }
//}