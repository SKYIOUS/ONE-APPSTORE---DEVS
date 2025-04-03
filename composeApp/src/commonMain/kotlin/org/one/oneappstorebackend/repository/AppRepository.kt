package org.one.oneappstorebackend.repository

import org.one.oneappstorebackend.model.AppMetadata
import org.one.oneappstorebackend.model.ReleaseChannel

/**
 * Repository interface for managing apps.
 */
interface AppRepository {
    /**
     * Gets all apps for a developer.
     * @param developerId The unique identifier for the developer.
     * @return A list of app metadata for the developer.
     */
    suspend fun getDeveloperApps(developerId: String): List<AppMetadata>
    
    /**
     * Gets an app by its ID.
     * @param developerId The unique identifier for the developer.
     * @param appId The unique identifier for the app.
     * @return The app metadata if found, null otherwise.
     */
    suspend fun getAppById(developerId: String, appId: String): AppMetadata?
    
    /**
     * Creates a new app.
     * @param developerId The unique identifier for the developer.
     * @param appMetadata The metadata of the app to create.
     * @param channel The release channel (stable or beta).
     * @return True if successful, false otherwise.
     */
    suspend fun createApp(developerId: String, appMetadata: AppMetadata, channel: ReleaseChannel): Boolean
    
    /**
     * Updates an existing app.
     * @param developerId The unique identifier for the developer.
     * @param appMetadata The updated metadata of the app.
     * @param channel The release channel (stable or beta).
     * @return True if successful, false otherwise.
     */
    suspend fun updateApp(developerId: String, appMetadata: AppMetadata, channel: ReleaseChannel): Boolean
    
    /**
     * Uploads an app package.
     * @param developerId The unique identifier for the developer.
     * @param appId The unique identifier for the app.
     * @param platform The platform identifier (android, ios, etc.).
     * @param fileName The name of the file to upload.
     * @param fileBytes The content of the file to upload.
     * @param channel The release channel (stable or beta).
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
     * @param releaseNotes The notes for the release.
     * @param channel The release channel (stable or beta).
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
     * Deletes an app.
     * @param developerId The unique identifier for the developer.
     * @param appId The unique identifier for the app.
     * @return True if successful, false otherwise.
     */
    suspend fun deleteApp(developerId: String, appId: String): Boolean
} 