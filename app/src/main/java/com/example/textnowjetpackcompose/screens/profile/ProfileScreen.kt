package com.example.textnowjetpackcompose.screens.profile

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.textnowjetpackcompose.R
import com.example.textnowjetpackcompose.navigation.DestinationScreen
import com.example.textnowjetpackcompose.screens.utils.PreferenceManager
import com.example.textnowjetpackcompose.viewmodels.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: AuthViewModel = koinViewModel(),
    navigate: (DestinationScreen) -> Unit
) {
    val context = LocalContext.current
    val preferenceManager = PreferenceManager(context)
    val profilePicUrl = remember { mutableStateOf(preferenceManager.getUser()?.profilePic) }
    val selectedImageUri = remember { mutableStateOf<ByteArray?>(null) }

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
                    Toast.makeText(context, "Failed to read image", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error selecting image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display selected image or current profile picture
        AsyncImage(
            model = selectedImageUri.value ?: profilePicUrl.value,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(125.dp)
                .clip(CircleShape),
            placeholder = painterResource(R.drawable.avatar),
            error = painterResource(R.drawable.avatar)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to launch gallery
        Button(onClick = { galleryLauncher.launch("image/*") }) {
            Text("Select Image")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Button to upload selected image
        Button(onClick = {
            viewModel.updateProfilePic(selectedImageUri.value)
        }) {
            Text("Upload Image")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Logout Button
        Button(onClick = {
            viewModel.logout()
            navigate(DestinationScreen.LoginScreenObj)
            Toast.makeText(context, "Logout Successful", Toast.LENGTH_SHORT).show()
        }) {
            Text("Logout")
        }
    }
}
