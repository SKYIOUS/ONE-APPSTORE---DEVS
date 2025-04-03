package org.one.oneappstorebackend.di

import org.koin.core.module.Module
import org.koin.dsl.module
import org.one.oneappstorebackend.auth.AuthHandler
import org.one.oneappstorebackend.auth.WebAuthHandler
import org.one.oneappstorebackend.service.GitHubService
import org.one.oneappstorebackend.viewmodel.AuthViewModel

/**
 * Factory class for creating web-specific Koin modules.
 */
object WebPlatformFactory {
    
    /**
     * Creates web-specific Koin module with platform implementations.
     */
    fun createPlatformModule(): Module = module {
        // Provide web-specific auth handler
        single<AuthHandler> { WebAuthHandler(get<GitHubService>()) }
        
        // Configure the AuthViewModel with web-specific authentication callback
        factory { 
            AuthViewModel(get(), get(), get()).apply {
                setAuthHandler(get<AuthHandler>())
                setAuthenticationCallback { get<AuthHandler>().authenticate() }
            }
        }
    }
} 