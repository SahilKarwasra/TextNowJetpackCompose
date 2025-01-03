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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.textnowjetpackcompose.R
import com.example.textnowjetpackcompose.data.model.SignupRequest
import com.example.textnowjetpackcompose.navigation.DestinationScreen
import com.example.textnowjetpackcompose.screens.auth.components.AuthTextField
import com.example.textnowjetpackcompose.screens.auth.components.ValidateSignupForm
import com.example.textnowjetpackcompose.screens.auth.components.ValidationResult
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel = koinViewModel(),
    navigate: (DestinationScreen) -> Unit
) {
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


        var usernameError by remember { mutableStateOf<String?>(null) }
        var emailError by remember { mutableStateOf<String?>(null) }
        var passwordError by remember { mutableStateOf<String?>(null) }
        var username by remember {
            mutableStateOf("")
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
        val context = LocalContext.current


        // Validation Logic
        val usernameValidation = ValidateSignupForm.validateUsername(username)
        usernameError =
            if (usernameValidation is ValidationResult.Invalid) {
                usernameValidation.message
            } else null


        val emailValidation = ValidateSignupForm.validateEmail(email)
        emailError =
            if (emailValidation is ValidationResult.Invalid) emailValidation.message else null

        val passwordValidation = ValidateSignupForm.validatePassword(password)
        passwordError =
            if (passwordValidation is ValidationResult.Invalid) passwordValidation.message else null


        val focusManager = LocalFocusManager.current


        Image(
            painter = painterResource(id = imageResource),
            contentDescription = null
        )
        Spacer(modifier = Modifier.padding(top = 20.dp))
        Text(
            text = "SignUp",
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.padding(top = 15.dp))
        AuthTextField(
            value = username,
            onValueChange = {
                username = it
            },
            placeholder = {
                Text(
                    text = "Username",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            isError = usernameError != null,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions (
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
            )
        Spacer(modifier = Modifier.padding(top = 12.dp))
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
            isError = emailError != null
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
            isError = passwordError != null,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions (
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )
        Spacer(modifier = Modifier.padding(top = 5.dp))


        Button(
            onClick = {

                val request = SignupRequest(
                    fullName = username,
                    email = email,
                    password = password
                )

                if (
                    usernameValidation is ValidationResult.Valid
                    && emailValidation is ValidationResult.Valid
                    && passwordValidation is ValidationResult.Valid
                ) {
                    viewModel.signup(request)
                } else {
                    val errorMessage = when {
                        usernameValidation is ValidationResult.Invalid -> usernameValidation.message
                        emailValidation is ValidationResult.Invalid -> emailValidation.message
                        passwordValidation is ValidationResult.Invalid -> passwordValidation.message
                        else -> "Invalid input"
                    }
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
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
                "SignUp"
            )
        }
        if (viewModel.errorMessage.value != null) {
            LaunchedEffect(viewModel.errorMessage.value) {
                Toast.makeText(context, viewModel.errorMessage.value, Toast.LENGTH_SHORT).show()
                viewModel.errorMessage.value = null
            }
        }
        if (viewModel.authUserResponse.value != null) {
            LaunchedEffect(viewModel.authUserResponse.value) {
                Toast.makeText(context, "Signup Successful", Toast.LENGTH_SHORT).show()
                viewModel.authUserResponse.value = null
                navigate(DestinationScreen.HomeScreenObj)
            }
        }
        Spacer(modifier = Modifier.padding(bottom = 25.dp))
        Row {
            Text(
                text = "Already have an Account? ",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
            )
            Text(
                text = "Login",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navigate(DestinationScreen.LoginScreenObj)
                }
            )
        }
    }
}