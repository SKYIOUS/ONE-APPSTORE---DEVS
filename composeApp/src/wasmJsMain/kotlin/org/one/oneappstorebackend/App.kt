package org.one.oneappstorebackend

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import org.jetbrains.skiko.wasm.onWasmReady
import org.koin.core.context.startKoin
import org.one.oneappstorebackend.auth.OAuthCallbackHandler
import org.one.oneappstorebackend.di.WebPlatformFactory
import org.one.oneappstorebackend.di.commonModule

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    // Initialize Koin DI
    startKoin {
        modules(
            commonModule(),
            WebPlatformFactory.createPlatformModule()
        )
    }
    
    // Initialize OAuth callback handler
    OAuthCallbackHandler.initialize()
    
    onWasmReady {
        CanvasBasedWindow("ONE App Store") {
            App()
        }
    }
} 