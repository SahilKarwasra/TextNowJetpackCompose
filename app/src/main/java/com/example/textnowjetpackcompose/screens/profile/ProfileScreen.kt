package com.example.textnowjetpackcompose.screens.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.textnowjetpackcompose.navigation.DestinationScreen
import com.example.textnowjetpackcompose.viewmodels.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(viewModel: AuthViewModel = koinViewModel(), navigate: (DestinationScreen) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

    Text("Profile Screen")
        val context = LocalContext.current
        Button(onClick = {
            viewModel.logout()
            navigate(DestinationScreen.LoginScreenObj)
            Toast.makeText(context, "Logout Successful", Toast.LENGTH_SHORT).show()
        }) {
            Text("Logout")
        }


    }
}
