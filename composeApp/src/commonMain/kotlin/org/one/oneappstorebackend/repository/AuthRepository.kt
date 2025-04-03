package org.one.oneappstorebackend.repository

import org.one.oneappstorebackend.model.DeveloperProfile

/**
 * Repository interface for handling authentication.
 */
interface AuthRepository {
    /**
     * Saves the authentication token.
     * @param token The token to save.
     */
    suspend fun saveAuthToken(token: String)
    
    /**
     * Gets the authentication token.
     * @return The token if available, null otherwise.
     */
    suspend fun getAuthToken(): String?
    
    /**
     * Checks if the user is authenticated.
     * @return True if the user is authenticated, false otherwise.
     */
    suspend fun isAuthenticated(): Boolean
    
    /**
     * Saves the developer profile.
     * @param profile The profile to save.
     */
    suspend fun saveDeveloperProfile(profile: DeveloperProfile)
    
    /**
     * Gets the developer profile.
     * @return The profile if available, null otherwise.
     */
    suspend fun getDeveloperProfile(): DeveloperProfile?
    
    /**
     * Logs out the user, clearing all authentication data.
     */
    suspend fun logout()
} 