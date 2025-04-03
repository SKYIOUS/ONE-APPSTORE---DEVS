package org.one.oneappstorebackend.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import org.one.oneappstorebackend.viewmodel.AuthViewModel

@Composable
fun AppListScreen(
    onLogout: () -> Unit,
    authViewModel: AuthViewModel = koinInject()
) {
    val developerProfile by authViewModel.developerProfile.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Developer Dashboard") },
                actions = {
                    Button(onClick = onLogout) {
                        Text("Logout")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Welcome, ${developerProfile?.displayName ?: "Developer"}!",
                    style = MaterialTheme.typography.h5
                )
                
                Text(
                    text = "Your apps will appear here soon.",
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
} 