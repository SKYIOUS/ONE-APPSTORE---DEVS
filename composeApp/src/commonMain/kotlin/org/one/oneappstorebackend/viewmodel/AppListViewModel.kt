package org.one.oneappstorebackend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.one.oneappstorebackend.model.AppMetadata
import org.one.oneappstorebackend.repository.AppRepository

/**
 * ViewModel for managing the list of apps in the UI.
 */
class AppListViewModel(
    private val appRepository: AppRepository
) : ViewModel() {
    
    private val _appListState = MutableStateFlow<AppListState>(AppListState.Loading)
    val appListState: StateFlow<AppListState> = _appListState
    
    /**
     * Loads the list of apps for a developer.
     * @param developerId The unique identifier for the developer.
     */
    fun loadApps(developerId: String) {
        viewModelScope.launch {
            try {
                _appListState.value = AppListState.Loading
                val apps = appRepository.getDeveloperApps(developerId)
                _appListState.value = if (apps.isEmpty()) {
                    AppListState.Empty
                } else {
                    AppListState.Success(apps)
                }
            } catch (e: Exception) {
                _appListState.value = AppListState.Error(e.message ?: "Failed to load apps")
            }
        }
    }
    
    /**
     * Refreshes the list of apps for a developer.
     * @param developerId The unique identifier for the developer.
     */
    fun refreshApps(developerId: String) {
        loadApps(developerId)
    }
    
    /**
     * Deletes an app.
     * @param developerId The unique identifier for the developer.
     * @param appId The unique identifier for the app.
     */
    fun deleteApp(developerId: String, appId: String) {
        viewModelScope.launch {
            try {
                val success = appRepository.deleteApp(developerId, appId)
                if (success) {
                    loadApps(developerId)
                } else {
                    _appListState.value = AppListState.Error("Failed to delete app")
                }
            } catch (e: Exception) {
                _appListState.value = AppListState.Error(e.message ?: "Failed to delete app")
            }
        }
    }
}

/**
 * Represents the state of the app list.
 */
sealed class AppListState {
    object Loading : AppListState()
    object Empty : AppListState()
    data class Success(val apps: List<AppMetadata>) : AppListState()
    data class Error(val message: String) : AppListState()
} 