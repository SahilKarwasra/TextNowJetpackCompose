package com.example.textnowjetpackcompose.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.textnowjetpackcompose.R
import com.example.textnowjetpackcompose.data.model.LoginRequest
import com.example.textnowjetpackcompose.navigation.DestinationScreen
import com.example.textnowjetpackcompose.screens.auth.components.AuthTextField
import com.example.textnowjetpackcompose.viewmodels.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen( navigate: (DestinationScreen) -> Unit,viewModel: AuthViewModel = koinViewModel()) {
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

        val focusManager = LocalFocusManager.current
        val context = LocalContext.current

        val userResponse by viewModel.userResponse.collectAsState()

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
            placeholder = {
                Text(
                    text = "Email",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions (
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
        )
        Spacer(modifier = Modifier.padding(top = 12.dp))
        AuthTextField(
            value = password,
            onValueChange = {
                password = it
            },

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
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions (
                onDone = {
                    focusManager.clearFocus()
                }
            ),
        )
        Spacer(modifier = Modifier.padding(top = 5.dp))
        Button(
            onClick = {
                val request = LoginRequest(
                    email = email,
                    password = password
                )
                try {
                    viewModel.login(request)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            },
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
        LaunchedEffect(userResponse) {
            userResponse?.let {
                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                navigate(DestinationScreen.HomeScreenObj)
            }
        }

        LaunchedEffect(viewModel.errorMessage.value) {
            viewModel.errorMessage.value?.let { errorMessage ->
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.errorMessage.value = null
            }
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
                    navigate(DestinationScreen.SignupScreenObj)
                }
            )
        }
    }
}