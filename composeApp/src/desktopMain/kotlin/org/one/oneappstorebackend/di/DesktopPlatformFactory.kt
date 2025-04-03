package org.one.oneappstorebackend.di

import org.koin.core.module.Module
import org.koin.dsl.module
import org.one.oneappstorebackend.auth.AuthHandler
import org.one.oneappstorebackend.auth.DesktopAuthHandler
import org.one.oneappstorebackend.service.GitHubService
import org.one.oneappstorebackend.viewmodel.AuthViewModel

/**
 * Factory class for creating desktop-specific Koin modules.
 */
object DesktopPlatformFactory {
    
    /**
     * Creates desktop-specific Koin module with platform implementations.
     */
    fun createPlatformModule(): Module = module {
        // Provide desktop-specific auth handler
        single<AuthHandler> { DesktopAuthHandler(get<GitHubService>()) }
        
        // Configure the AuthViewModel with desktop-specific authentication callback
        factory { 
            AuthViewModel(get(), get(), get()).apply {
                setAuthHandler(get<AuthHandler>())
                setAuthenticationCallback { get<AuthHandler>().authenticate() }
            }
        }
    }
} 