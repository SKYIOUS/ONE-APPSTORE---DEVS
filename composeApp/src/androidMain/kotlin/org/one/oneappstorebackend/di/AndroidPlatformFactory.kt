package org.one.oneappstorebackend.di

import android.content.Context
import androidx.activity.ComponentActivity
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import org.one.oneappstorebackend.auth.AndroidAuthHandler
import org.one.oneappstorebackend.auth.AuthHandler
import org.one.oneappstorebackend.service.GitHubService
import org.one.oneappstorebackend.viewmodel.AuthViewModel

/**
 * Factory class for creating Android-specific Koin modules.
 */
object AndroidPlatformFactory {
    
    /**
     * Creates Android-specific Koin module with platform implementations.
     * 
     * @param activity The main activity of the app, used for launching browser for OAuth
     */
    fun createPlatformModule(activity: ComponentActivity? = null): Module = module {
        // Provide Android-specific auth handler
        single<AuthHandler> { 
            AndroidAuthHandler(
                context = get<Context>(),
                activity = activity,
                githubService = get<GitHubService>()
            ).apply {
                // Register the instance for deep link handling
                AndroidAuthHandler.register(this)
            }
        }
        
        // Configure the AuthViewModel with Android-specific authentication callback
        factory { 
            AuthViewModel(get(), get(), get()).apply {
                setAuthHandler(get<AuthHandler>())
                setAuthenticationCallback { get<AuthHandler>().authenticate() }
            }
        }
    }
} 