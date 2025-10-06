package com.example.textnowjetpackcompose.features.profile.presentation.screen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.textnowjetpackcompose.R
import com.example.textnowjetpackcompose.config.PreferenceManager
import com.example.textnowjetpackcompose.config.navigation.DestinationScreen
import com.example.textnowjetpackcompose.features.auth.presentation.viewmodel.AuthState
import com.example.textnowjetpackcompose.features.auth.presentation.viewmodel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    navigate: (DestinationScreen) -> Unit,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {

    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    val isProfilePicUpdating by authViewModel.isProfilePicUpdating.collectAsStateWithLifecycle()
    val selectedImageUri = authViewModel.selectedImageUri
    val context = LocalContext.current
    val profilePicUrl by authViewModel.profilePicUrl.collectAsStateWithLifecycle()


    LaunchedEffect(authState) {
        if (authState == AuthState.UnAuthenticated) {
            navigate(
                DestinationScreen.SubGraphAuth
            )
            snackbarHostState.showSnackbar(
                message = "Logged Out Successfully",
                withDismissAction = true
            )
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes()
                if (bytes != null) {
                    selectedImageUri.value = bytes
                } else {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Failed to read image",
                            withDismissAction = true
                        )
                    }
                }
            } catch (e: Exception) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Error selecting image",
                        withDismissAction = true
                    )
                }
            }
        } else {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = "No image selected",
                    withDismissAction = true
                )
            }
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = selectedImageUri.value ?: profilePicUrl,
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(125.dp)
                    .clip(CircleShape),
                placeholder = painterResource(R.drawable.avatar),
                error = painterResource(R.drawable.avatar)
            )

            if (isProfilePicUpdating) {
                CircularWavyProgressIndicator()
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { galleryLauncher.launch("image/*") }) {
            Text(text = if (selectedImageUri.value == null) "Select Image" else "Change Image")
        }

        AnimatedVisibility(
            visible = selectedImageUri.value != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                authViewModel.updateProfilePic(selectedImageUri.value) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = it,
                            withDismissAction = true
                        )
                    }
                }
            }) {
                Text(text = "Upload Image")
            }
        }


        Spacer(modifier = Modifier.height(32.dp))


        Button(
            onClick = {
                authViewModel.logout()
            },
            modifier = Modifier
                .padding(18.dp)
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White,
                disabledContainerColor = Color.Red.copy(alpha = 0.7f),
                disabledContentColor = Color.White.copy(alpha = 0.7f)
            )
        ) {
            BasicText(
                "Logout",
                maxLines = 1,
                autoSize = TextAutoSize.StepBased(maxFontSize = 20.sp),
                style = TextStyle(
                    color = Color.White,
                )
            )
        }
    }
}