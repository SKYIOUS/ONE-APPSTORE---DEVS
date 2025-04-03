package org.one.oneappstorebackend.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.one.oneappstorebackend.viewmodel.AuthState
import org.one.oneappstorebackend.viewmodel.AuthViewModel

import oneappstore_devs.composeapp.generated.resources.Res
import oneappstore_devs.composeapp.generated.resources.compose_multiplatform

/**
 * Login screen for the app.
 * @param onAuthSuccess Callback when authentication is successful.
 */
@Composable
fun LoginScreen(
    onAuthSuccess: () -> Unit
) {
    val viewModel: AuthViewModel = koinInject()
    val authState by viewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> onAuthSuccess()
            is AuthState.Error -> {
                snackbarHostState.showSnackbar(
                    (authState as AuthState.Error).message
                )
            }
            else -> {}
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Developer Login") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.compose_multiplatform),
                contentDescription = "App Logo",
                modifier = Modifier.size(120.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Multiplatform App Store",
                style = MaterialTheme.typography.h5
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Developer Portal",
                style = MaterialTheme.typography.subtitle1
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            when (authState) {
                is AuthState.Loading -> {
                    CircularProgressIndicator()
                }
                is AuthState.Unauthenticated, is AuthState.Error -> {
                    Button(
                        onClick = { viewModel.authenticate() },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text("Login with GitHub")
                    }
                }
                else -> {}
            }
        }
    }
} 