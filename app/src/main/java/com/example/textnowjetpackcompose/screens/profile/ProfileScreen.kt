package com.example.textnowjetpackcompose.screens.profile

import android.util.Base64
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.Uri
import coil3.compose.AsyncImage
import coil3.toAndroidUri
import coil3.toCoilUri
import com.example.textnowjetpackcompose.R
import com.example.textnowjetpackcompose.navigation.DestinationScreen
import com.example.textnowjetpackcompose.screens.utils.PreferenceManager
import com.example.textnowjetpackcompose.viewmodels.AuthViewModel
import com.example.textnowjetpackcompose.viewmodels.ChatViewModels
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: AuthViewModel = koinViewModel(),
    navigate: (DestinationScreen) -> Unit
) {
    val context = LocalContext.current
    val preferenceManager = PreferenceManager(context)
    val profilePicUrl = remember { mutableStateOf(preferenceManager.getUser()?.profilePic) }
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        if (uri != null) {
            selectedImageUri.value = uri.toCoilUri()
        } else {
            Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        AsyncImage(
            model = selectedImageUri.value ?: profilePicUrl.value,
            contentDescription = "",
            modifier = Modifier
                .size(125.dp)
                .clip(CircleShape),
            placeholder = painterResource(R.drawable.avatar),
            error = painterResource(R.drawable.avatar)
        )


        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { galleryLauncher.launch("image/*") }) {
            Text("Select Image")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            selectedImageUri.value?.let { uri ->
                // Convert URI to File or Base64 and upload
                val inputStream = context.contentResolver.openInputStream(uri.toAndroidUri())
                val fileBytes = inputStream?.readBytes()

                if (fileBytes != null) {
                    // Convert ByteArray to Base64 string
                    val base64Image = Base64.encodeToString(fileBytes, Base64.DEFAULT)
                    // Pass the Base64 string to the ViewModel
                    viewModel.updateProfilePic(base64Image)
                    Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to process image", Toast.LENGTH_SHORT).show()
                }
            } ?: Toast.makeText(context, "No image selected to upload", Toast.LENGTH_SHORT).show()
        }) {
            Text("Upload Image")
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.logout()
            navigate(DestinationScreen.LoginScreenObj)
            Toast.makeText(context, "Logout Successful", Toast.LENGTH_SHORT).show()
        }) {
            Text("Logout")
        }
    }
}
