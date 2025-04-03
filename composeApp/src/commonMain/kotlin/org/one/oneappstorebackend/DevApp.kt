package org.one.oneappstorebackend

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.one.oneappstorebackend.di.appModule
import org.one.oneappstorebackend.navigation.Routes
import org.one.oneappstorebackend.ui.screens.AppListScreen
import org.one.oneappstorebackend.ui.screens.LoginScreen
import org.one.oneappstorebackend.viewmodel.AuthState
import org.one.oneappstorebackend.viewmodel.AuthViewModel

/**
 * Main app composable for the Developer app.
 */
@Composable
fun DevApp() {
    KoinApplication(application = {
        modules(appModule)
    }) {
        MaterialTheme {
            MainNavigation()
        }
    }
}

/**
 * Main navigation for the app.
 */
@Composable
private fun MainNavigation() {
    var currentRoute by remember { mutableStateOf(Routes.LOGIN) }
    val authViewModel: AuthViewModel = koinInject()
    val authState by authViewModel.authState.collectAsState()
    
    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated && currentRoute == Routes.LOGIN) {
            currentRoute = Routes.APP_LIST
        } else if (authState is AuthState.Unauthenticated && currentRoute != Routes.LOGIN) {
            currentRoute = Routes.LOGIN
        }
    }
    
    when (currentRoute) {
        Routes.LOGIN -> {
            LoginScreen(
                onAuthSuccess = {
                    currentRoute = Routes.APP_LIST
                }
            )
        }
        Routes.APP_LIST -> {
            AppListScreen(
                onAppClick = { appId ->
                    currentRoute = Routes.appDetail(appId)
                },
                onCreateAppClick = {
                    currentRoute = Routes.APP_CREATE
                },
                onProfileClick = {
                    currentRoute = Routes.PROFILE
                }
            )
        }
        // TODO: Add more routes
        else -> {
            // TODO: Handle other routes
        }
    }
} 