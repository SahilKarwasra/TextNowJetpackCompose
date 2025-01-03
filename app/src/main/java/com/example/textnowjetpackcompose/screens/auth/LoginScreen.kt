package com.example.textnowjetpackcompose.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.textnowjetpackcompose.R
import com.example.textnowjetpackcompose.navigation.DestinationScreen
import com.example.textnowjetpackcompose.screens.auth.components.AuthTextField

@Composable
fun LoginScreen(modifier: Modifier = Modifier, navigate: (DestinationScreen) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val isDarkMode = isSystemInDarkTheme()
        val imageResource = if (isDarkMode) {
            R.drawable.textnow
        } else {
            R.drawable.textnow1
        }

        var email by remember {
            mutableStateOf("")
        }
        var password by remember {
            mutableStateOf("")
        }
        var showPassword by rememberSaveable {
            mutableStateOf(false)
        }

        Image(
            painter = painterResource(id = imageResource),
            contentDescription = null
        )
        Spacer(modifier = Modifier.padding(top = 20.dp))
        Text(
            text = "Login",
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.padding(top = 15.dp))
        AuthTextField(
            value = email,
            onValueChange = {
                email = it
            },
            keyboardType = KeyboardType.Text,
            placeholder = {
                Text(
                    text = "Email",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
        Spacer(modifier = Modifier.padding(top = 12.dp))
        AuthTextField(
            value = password,
            onValueChange = {
                password = it
            },

            keyboardType = KeyboardType.Password,
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (!showPassword)
                            Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            placeholder = {
                Text(
                    text = "Password",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
        )
        Spacer(modifier = Modifier.padding(top = 5.dp))
        Button(
            onClick = {},
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                disabledContentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text(
                "Login"
            )
        }
        Spacer(modifier = Modifier.padding(bottom = 25.dp))
        Row {
            Text(
                text = "Don't have an Account? ",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
            )
            Text(
                text = "SignUp",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                }
            )
        }
    }
}