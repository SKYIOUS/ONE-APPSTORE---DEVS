package org.one.oneappstorebackend.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import org.one.oneappstorebackend.viewmodel.AuthState
import org.one.oneappstorebackend.viewmodel.AuthViewModel

/**
 * Login screen for the app.
 * @param onLoginSuccess Callback when authentication is successful.
 */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    authViewModel: AuthViewModel = koinInject()
) {
    val authState by authViewModel.authState.collectAsState()
    
    // Check if already authenticated
    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            onLoginSuccess()
        }
    }
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Logo
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                // Replace with your app logo
                Text("ONE", style = MaterialTheme.typography.h3)
            }
            
            // App name
            Text(
                text = "ONE App Store",
                style = MaterialTheme.typography.h5
            )
            
            // Description
            Text(
                text = "Developer Portal",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Login button
            Button(
                onClick = { authViewModel.authenticate() },
                enabled = authState !is AuthState.Loading
            ) {
                Text("Login with GitHub")
            }
            
            // Show loading indicator if authenticating
            if (authState is AuthState.Loading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
            
            // Show error message if authentication failed
            if (authState is AuthState.Error) {
                Text(
                    text = (authState as AuthState.Error).message,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
} 