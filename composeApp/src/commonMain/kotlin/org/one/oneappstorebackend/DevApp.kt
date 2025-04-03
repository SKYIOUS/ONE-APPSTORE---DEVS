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
import org.one.oneappstorebackend.di.commonModule
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
        modules(commonModule())
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
fun MainNavigation() {
    var currentRoute by remember { mutableStateOf(Routes.Login) }
    val authViewModel = koinInject<AuthViewModel>()
    val authState by authViewModel.authState.collectAsState()
    
    // Check if user is already authenticated
    LaunchedEffect(authState) {
        currentRoute = when (authState) {
            is AuthState.Authenticated -> Routes.AppList
            is AuthState.Unauthenticated -> Routes.Login
            else -> currentRoute
        }
    }
    
    when (currentRoute) {
        Routes.Login -> LoginScreen(
            onLoginSuccess = { currentRoute = Routes.AppList },
            authViewModel = authViewModel
        )
        Routes.AppList -> AppListScreen(
            onLogout = { 
                authViewModel.logout()
                currentRoute = Routes.Login
            }
        )
    }
} 