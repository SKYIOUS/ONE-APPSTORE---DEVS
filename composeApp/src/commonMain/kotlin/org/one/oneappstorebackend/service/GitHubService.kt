package org.one.oneappstorebackend.service

import org.one.oneappstorebackend.model.DeveloperProfile
import org.one.oneappstorebackend.model.AppMetadata
import org.one.oneappstorebackend.model.ReleaseChannel

/**
 * Service interface for interacting with GitHub as the backend for the app store.
 */
interface GitHubService {
    /**
     * Authenticates the user with GitHub using OAuth.
     * @return A token if authentication is successful, null otherwise.
     */
    suspend fun authenticate(): String?
    
    /**
     * Completes the authentication process by exchanging a code for a token.
     * This is called by platform-specific authentication handlers.
     * @param code The authorization code received from GitHub OAuth.
     * @return The access token if successful, null otherwise.
     */
    suspend fun completeAuthentication(code: String): String?
    
    /**
     * Gets the user profile from GitHub after authentication.
     * @return The developer profile if successful, null otherwise.
     */
    suspend fun getDeveloperProfile(): DeveloperProfile?
    
    /**
     * Creates a new repository for the developer if they don't already have one.
     * @param developerId The unique identifier for the developer.
     * @return The URL of the created repository if successful, null otherwise.
     */
    suspend fun createDeveloperRepository(developerId: String): String?
    
    /**
     * Adds the developer's repository as a submodule to the main app store repository.
     * @param developerId The unique identifier for the developer.
     * @param repoUrl The URL of the developer's repository.
     * @return True if successful, false otherwise.
     */
    suspend fun addRepositoryAsSubmodule(developerId: String, repoUrl: String): Boolean
    
    /**
     * Creates the basic directory structure for a developer in their repository.
     * @param developerId The unique identifier for the developer.
     * @return True if successful, false otherwise.
     */
    suspend fun setupDeveloperDirectoryStructure(developerId: String): Boolean
    
    /**
     * Uploads app metadata to the developer's repository.
     * @param developerId The unique identifier for the developer.
     * @param appMetadata The metadata of the app to upload.
     * @param channel The release channel to upload to.
     * @return True if successful, false otherwise.
     */
    suspend fun uploadAppMetadata(developerId: String, appMetadata: AppMetadata, channel: ReleaseChannel): Boolean
    
    /**
     * Uploads an app package (APK, IPA, etc) to the developer's repository.
     * @param developerId The unique identifier for the developer.
     * @param appId The unique identifier for the app.
     * @param platform The platform of the app (android, ios, etc).
     * @param fileName The name of the file to upload.
     * @param fileBytes The bytes of the file to upload.
     * @param channel The release channel to upload to.
     * @return The URL of the uploaded file if successful, null otherwise.
     */
    suspend fun uploadAppPackage(
        developerId: String, 
        appId: String, 
        platform: String, 
        fileName: String, 
        fileBytes: ByteArray, 
        channel: ReleaseChannel
    ): String?
    
    /**
     * Creates a new release for an app.
     * @param developerId The unique identifier for the developer.
     * @param appId The unique identifier for the app.
     * @param version The version of the release.
     * @param releaseNotes The release notes.
     * @param channel The release channel to create the release in.
     * @return The URL of the created release if successful, null otherwise.
     */
    suspend fun createRelease(
        developerId: String,
        appId: String,
        version: String,
        releaseNotes: String,
        channel: ReleaseChannel
    ): String?
    
    /**
     * Retrieves all apps for a developer.
     * @param developerId The unique identifier for the developer.
     * @return A list of app metadata if successful, empty list otherwise.
     */
    suspend fun getDeveloperApps(developerId: String): List<AppMetadata>
    
    /**
     * Updates the reference to the developer's repository in the main app store repository.
     * @param developerId The unique identifier for the developer.
     * @return True if successful, false otherwise.
     */
    suspend fun updateSubmoduleReference(developerId: String): Boolean
} 