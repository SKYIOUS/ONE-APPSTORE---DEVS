package org.one.oneappstorebackend.auth

/**
 * Interface for platform-specific authentication handlers.
 * Implementations should provide platform-specific OAuth functionality for GitHub.
 */
interface AuthHandler {
    /**
     * Authenticate the user with GitHub.
     * This method should implement the platform-specific authentication flow.
     * 
     * @return The GitHub access token if authentication was successful, or an empty string if it failed
     */
    suspend fun authenticate(): String
} 