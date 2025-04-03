package org.one.oneappstorebackend

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.core.context.startKoin
import org.one.oneappstorebackend.auth.DesktopAuthHandler
import org.one.oneappstorebackend.di.commonModule
import org.one.oneappstorebackend.di.DesktopPlatformFactory
import org.one.oneappstorebackend.service.GitHubServiceImpl
import org.one.oneappstorebackend.viewmodel.AuthViewModel

fun main() = application {
    // Start Koin dependency injection
    startKoin {
        modules(
            commonModule(),
            DesktopPlatformFactory.createPlatformModule()
        )
    }
    
    // Create our app window
    Window(
        onCloseRequest = ::exitApplication,
        title = "One App Store - Developer Portal"
    ) {
        App()
    }
}

/**
 * Initializes the auth handling on desktop platform.
 */
fun initDesktopAuth(authViewModel: AuthViewModel) {
    val gitHubService = authViewModel.getGitHubService() as GitHubServiceImpl
    val desktopAuthHandler = DesktopAuthHandler(gitHubService)
    
    // Set the auth handler in the ViewModel
    authViewModel.setAuthHandler(desktopAuthHandler)
    
    // Override the authenticate function to use the desktop handler
    authViewModel.setAuthenticationCallback { desktopAuthHandler.authenticate() }
}