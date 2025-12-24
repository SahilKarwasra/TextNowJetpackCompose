package com.example.textnowjetpackcompose.features.auth.presentation.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.textnowjetpackcompose.R
import com.example.textnowjetpackcompose.features.auth.domain.model.LoginRequest
import com.example.textnowjetpackcompose.features.auth.presentation.components.AuthTextField
import com.example.textnowjetpackcompose.features.auth.presentation.components.ValidateSignupForm
import com.example.textnowjetpackcompose.features.auth.presentation.components.ValidationResult
import com.example.textnowjetpackcompose.features.auth.presentation.viewmodel.AuthState
import com.example.textnowjetpackcompose.features.auth.presentation.viewmodel.AuthViewModel
import com.example.textnowjetpackcompose.config.navigation.DestinationScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    navigate: (DestinationScreen) -> Unit,
    viewModel: AuthViewModel,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {

    val state by viewModel.authState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val imeHeigh = remember { mutableIntStateOf(0) }
    val ime = WindowInsets.ime
    val localDensity = LocalDensity.current
    LaunchedEffect(key1 = Unit) {
        val keyboardFlow = snapshotFlow {
            ime.getBottom(localDensity)
        }

        keyboardFlow.collect { keyboardHeight ->
            if (keyboardHeight > 0) {
                if (imeHeigh.intValue < keyboardHeight) {
                    scrollState.scrollBy((keyboardHeight - imeHeigh.intValue).toFloat())
                }
                imeHeigh.intValue = keyboardHeight
            } else if (keyboardHeight == 0) {
                imeHeigh.intValue = 0
            }
        }
    }


    LaunchedEffect(state) {
        when (state) {
            is AuthState.Authenticated -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Logged In Successfully",
                        duration = SnackbarDuration.Short
                    )
                }
                delay(800)
                navigate(
                    DestinationScreen.HomeScreenObj
                )
            }

            is AuthState.Error -> {
                val message = (state as AuthState.Error).message
                Log.d("LoginFailed", "LoginScreen: $message")
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = message,
                        duration = SnackbarDuration.Short
                    )
                }
            }

            else -> {}
        }
    }

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
        val focusManager = LocalFocusManager.current
        var showPassword by rememberSaveable {
            mutableStateOf(false)
        }

        val state = viewModel.formState

        Column(
            modifier = Modifier
                .imePadding()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = imageResource),
                contentDescription = null,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            Text(
                text = "Login",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 15.dp)
            )

            AuthTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChange,
                placeholder = {
                    Text(
                        text = "Email",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            AuthTextField(
                value = state.password,
                onValueChange = viewModel::onPasswordChange,

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
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier.padding(bottom = 5.dp)
            )
            Button(
                onClick = {
                    val emailResult = ValidateSignupForm.validateEmail(state.email)
                    val passwordResult = ValidateSignupForm.validatePassword(state.password)
                    when {
                        emailResult is ValidationResult.Invalid -> {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = emailResult.message,
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }

                        passwordResult is ValidationResult.Invalid -> {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = passwordResult.message,
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }

                        else -> {
                            val request = LoginRequest(
                                email = state.email,
                                password = state.password
                            )
                            viewModel.login(request)
                        }
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
