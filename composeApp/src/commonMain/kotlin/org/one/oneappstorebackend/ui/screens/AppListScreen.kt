package org.one.oneappstorebackend.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import org.one.oneappstorebackend.model.AppMetadata
import org.one.oneappstorebackend.model.PlatformType
import org.one.oneappstorebackend.viewmodel.AppListState
import org.one.oneappstorebackend.viewmodel.AppListViewModel
import org.one.oneappstorebackend.viewmodel.AuthViewModel

/**
 * App list screen showing all apps for the developer.
 * @param onAppClick Callback when an app is clicked.
 * @param onCreateAppClick Callback when the create app button is clicked.
 * @param onProfileClick Callback when the profile button is clicked.
 */
@Composable
fun AppListScreen(
    onAppClick: (String) -> Unit,
    onCreateAppClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val authViewModel: AuthViewModel = koinInject()
    val developerProfile by authViewModel.developerProfile.collectAsState()
    val appListViewModel: AppListViewModel = koinInject()
    val appListState by appListViewModel.appListState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(developerProfile) {
        developerProfile?.let { profile ->
            appListViewModel.loadApps(profile.id)
        }
    }
    
    LaunchedEffect(appListState) {
        when (appListState) {
            is AppListState.Error -> {
                snackbarHostState.showSnackbar(
                    (appListState as AppListState.Error).message
                )
            }
            else -> {}
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Apps") },
                actions = {
                    // TODO: Add profile button
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("New App") },
                icon = { Icon(Icons.Filled.Add, contentDescription = "Add") },
                onClick = onCreateAppClick
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (appListState) {
                is AppListState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is AppListState.Empty -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No apps yet",
                            style = MaterialTheme.typography.h6
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Click the + button to create your first app",
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
                is AppListState.Success -> {
                    val apps = (appListState as AppListState.Success).apps
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(apps) { app ->
                            AppItem(
                                app = app,
                                onClick = { onAppClick(app.id) }
                            )
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

/**
 * App item in the list.
 * @param app The app metadata.
 * @param onClick Callback when the item is clicked.
 */
@Composable
private fun AppItem(
    app: AppMetadata,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TODO: Add app icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .padding(4.dp)
            ) {
                
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = app.name,
                    style = MaterialTheme.typography.h6,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = app.shortDescription,
                    style = MaterialTheme.typography.body2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "v${app.version}",
                    style = MaterialTheme.typography.caption
                )
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Platforms:",
                    style = MaterialTheme.typography.caption
                )
                
                Text(
                    text = app.platforms.joinToString { it.type.name },
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
} 