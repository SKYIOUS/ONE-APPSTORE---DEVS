package org.one.oneappstorebackend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.one.oneappstorebackend.model.AppMetadata
import org.one.oneappstorebackend.model.ReleaseChannel
import org.one.oneappstorebackend.repository.AppRepository

/**
 * ViewModel for creating and editing apps in the UI.
 */
class AppEditViewModel(
    private val appRepository: AppRepository
) : ViewModel() {
    
    private val _appEditState = MutableStateFlow<AppEditState>(AppEditState.Idle)
    val appEditState: StateFlow<AppEditState> = _appEditState
    
    private val _currentApp = MutableStateFlow<AppMetadata?>(null)
    val currentApp: StateFlow<AppMetadata?> = _currentApp
    
    /**
     * Loads an app for editing.
     * @param developerId The unique identifier for the developer.
     * @param appId The unique identifier for the app.
     */
    fun loadApp(developerId: String, appId: String) {
        viewModelScope.launch {
            try {
                _appEditState.value = AppEditState.Loading
                val app = appRepository.getAppById(developerId, appId)
                if (app != null) {
                    _currentApp.value = app
                    _appEditState.value = AppEditState.Loaded
                } else {
                    _appEditState.value = AppEditState.Error("App not found")
                }
            } catch (e: Exception) {
                _appEditState.value = AppEditState.Error(e.message ?: "Failed to load app")
            }
        }
    }
    
    /**
     * Creates a new app.
     * @param developerId The unique identifier for the developer.
     * @param appMetadata The metadata of the app to create.
     * @param channel The release channel (stable or beta).
     */
    fun createApp(developerId: String, appMetadata: AppMetadata, channel: ReleaseChannel) {
        viewModelScope.launch {
            try {
                _appEditState.value = AppEditState.Saving
                val success = appRepository.createApp(developerId, appMetadata, channel)
                if (success) {
                    _currentApp.value = appMetadata
                    _appEditState.value = AppEditState.Saved
                } else {
                    _appEditState.value = AppEditState.Error("Failed to create app")
                }
            } catch (e: Exception) {
                _appEditState.value = AppEditState.Error(e.message ?: "Failed to create app")
            }
        }
    }
    
    /**
     * Updates an existing app.
     * @param developerId The unique identifier for the developer.
     * @param appMetadata The updated metadata of the app.
     * @param channel The release channel (stable or beta).
     */
    fun updateApp(developerId: String, appMetadata: AppMetadata, channel: ReleaseChannel) {
        viewModelScope.launch {
            try {
                _appEditState.value = AppEditState.Saving
                val success = appRepository.updateApp(developerId, appMetadata, channel)
                if (success) {
                    _currentApp.value = appMetadata
                    _appEditState.value = AppEditState.Saved
                } else {
                    _appEditState.value = AppEditState.Error("Failed to update app")
                }
            } catch (e: Exception) {
                _appEditState.value = AppEditState.Error(e.message ?: "Failed to update app")
            }
        }
    }
    
    /**
     * Uploads an app package.
     * @param developerId The unique identifier for the developer.
     * @param appId The unique identifier for the app.
     * @param platform The platform identifier (android, ios, etc.).
     * @param fileName The name of the file to upload.
     * @param fileBytes The content of the file to upload.
     * @param channel The release channel (stable or beta).
     */
    fun uploadAppPackage(
        developerId: String, 
        appId: String, 
        platform: String, 
        fileName: String, 
        fileBytes: ByteArray,
        channel: ReleaseChannel
    ) {
        viewModelScope.launch {
            try {
                _appEditState.value = AppEditState.Uploading
                val url = appRepository.uploadAppPackage(
                    developerId, appId, platform, fileName, fileBytes, channel
                )
                if (url != null) {
                    _appEditState.value = AppEditState.Uploaded(url)
                } else {
                    _appEditState.value = AppEditState.Error("Failed to upload package")
                }
            } catch (e: Exception) {
                _appEditState.value = AppEditState.Error(e.message ?: "Failed to upload package")
            }
        }
    }
    
    /**
     * Creates a new release for an app.
     * @param developerId The unique identifier for the developer.
     * @param appId The unique identifier for the app.
     * @param version The version of the release.
     * @param releaseNotes The notes for the release.
     * @param channel The release channel (stable or beta).
     */
    fun createRelease(
        developerId: String, 
        appId: String, 
        version: String, 
        releaseNotes: String,
        channel: ReleaseChannel
    ) {
        viewModelScope.launch {
            try {
                _appEditState.value = AppEditState.Creating
                val url = appRepository.createRelease(
                    developerId, appId, version, releaseNotes, channel
                )
                if (url != null) {
                    _appEditState.value = AppEditState.Created(url)
                } else {
                    _appEditState.value = AppEditState.Error("Failed to create release")
                }
            } catch (e: Exception) {
                _appEditState.value = AppEditState.Error(e.message ?: "Failed to create release")
            }
        }
    }
    
    /**
     * Resets the state to idle.
     */
    fun resetState() {
        _appEditState.value = AppEditState.Idle
    }
}

/**
 * Represents the state of app editing.
 */
sealed class AppEditState {
    object Idle : AppEditState()
    object Loading : AppEditState()
    object Loaded : AppEditState()
    object Saving : AppEditState()
    object Saved : AppEditState()
    object Uploading : AppEditState()
    data class Uploaded(val url: String) : AppEditState()
    object Creating : AppEditState()
    data class Created(val url: String) : AppEditState()
    data class Error(val message: String) : AppEditState()
} 