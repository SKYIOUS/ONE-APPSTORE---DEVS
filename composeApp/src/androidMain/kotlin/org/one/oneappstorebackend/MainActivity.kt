package org.one.oneappstorebackend

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.one.oneappstorebackend.auth.AndroidAuthHandler
import org.one.oneappstorebackend.di.AndroidPlatformFactory
import org.one.oneappstorebackend.di.commonModule

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Koin DI
        startKoin {
            androidContext(applicationContext)
            modules(
                commonModule(),
                AndroidPlatformFactory.createPlatformModule(this@MainActivity)
            )
        }
        
        // Handle initial intent if it's a deep link
        handleIntent(intent)
        
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    App()
                }
            }
        }
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }
    
    private fun handleIntent(intent: Intent?) {
        // Check if the intent is a deep link from GitHub OAuth
        if (intent?.data != null) {
            AndroidAuthHandler.getInstance()?.handleDeepLink(intent.data!!)
        }
    }
    
    override fun onDestroy() {
        // Unregister the auth handler to prevent memory leaks
        AndroidAuthHandler.getInstance()?.let { AndroidAuthHandler.unregister(it) }
        super.onDestroy()
    }
}