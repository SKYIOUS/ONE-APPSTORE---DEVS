package org.one.oneappstorebackend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.one.oneappstorebackend.model.DeveloperProfile
import org.one.oneappstorebackend.repository.AuthRepository
import org.one.oneappstorebackend.service.GitHubService

/**
 * ViewModel for handling authentication in the UI.
 */
class AuthViewModel(
    private val authRepository: AuthRepository,
    private val gitHubService: GitHubService
) : ViewModel() {
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState
    
    private val _developerProfile = MutableStateFlow<DeveloperProfile?>(null)
    val developerProfile: StateFlow<DeveloperProfile?> = _developerProfile
    
    // Platform-specific auth handler
    private var authHandler: Any? = null
    
    init {
        viewModelScope.launch {
            if (authRepository.isAuthenticated()) {
                _authState.value = AuthState.Authenticated
                loadDeveloperProfile()
            } else {
                _authState.value = AuthState.Unauthenticated
            }
        }
    }
    
    /**
     * Sets the platform-specific auth handler.
     */
    fun setAuthHandler(handler: Any) {
        authHandler = handler
    }
    
    /**
     * Gets the GitHub service for platform-specific implementations.
     */
    fun getGitHubService(): GitHubService {
        return gitHubService
    }
    
    /**
     * Authenticates the user with GitHub.
     * This will use the platform-specific auth handler.
     */
    fun authenticate() {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                
                // Platform-specific authentication will be handled here
                // The actual implementation depends on the platform
                val token = gitHubService.authenticate()
                
                if (token != null) {
                    authRepository.saveAuthToken(token)
                    _authState.value = AuthState.Authenticated
                    loadDeveloperProfile()
                } else {
                    _authState.value = AuthState.Error("Authentication failed")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    /**
     * Loads the developer profile.
     */
    private fun loadDeveloperProfile() {
        viewModelScope.launch {
            try {
                val profile = authRepository.getDeveloperProfile() ?: gitHubService.getDeveloperProfile()
                if (profile != null) {
                    authRepository.saveDeveloperProfile(profile)
                    _developerProfile.value = profile
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Failed to load profile")
            }
        }
    }
    
    /**
     * Logs out the user.
     */
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _authState.value = AuthState.Unauthenticated
            _developerProfile.value = null
        }
    }
}

/**
 * Represents the state of authentication.
 */
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
} 