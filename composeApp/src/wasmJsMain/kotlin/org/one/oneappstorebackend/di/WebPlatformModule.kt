package org.one.oneappstorebackend.di

import com.russhwolf.settings.JsSettings
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.one.oneappstorebackend.model.DeveloperProfile
import org.one.oneappstorebackend.repository.AuthRepository

/**
 * Creates an HttpClient for Web.
 */
actual fun createHttpClient(): Any {
    return HttpClient(Js) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }
}

/**
 * Creates Settings for Web using localStorage.
 */
actual fun createSettings(): Any {
    return JsSettings()
}

/**
 * Web implementation of AuthRepository.
 */
actual class PlatformAuthRepository actual constructor(settings: Any) : AuthRepository {
    private val secureSettings = settings as Settings
    
    override suspend fun saveAuthToken(token: String) {
        secureSettings.putString(AUTH_TOKEN_KEY, token)
    }
    
    override suspend fun getAuthToken(): String? {
        return secureSettings.getStringOrNull(AUTH_TOKEN_KEY)
    }
    
    override suspend fun isAuthenticated(): Boolean {
        return getAuthToken() != null
    }
    
    override suspend fun saveDeveloperProfile(profile: DeveloperProfile) {
        secureSettings.putString(PROFILE_ID_KEY, profile.id)
        secureSettings.putString(PROFILE_USERNAME_KEY, profile.githubUsername)
        secureSettings.putString(PROFILE_DISPLAY_NAME_KEY, profile.displayName)
        secureSettings.putString(PROFILE_AVATAR_URL_KEY, profile.avatarUrl)
        secureSettings.putString(PROFILE_BIO_KEY, profile.bio)
        secureSettings.putString(PROFILE_EMAIL_KEY, profile.email)
        secureSettings.putString(PROFILE_WEBSITE_KEY, profile.website)
        secureSettings.putString(PROFILE_REPO_URL_KEY, profile.githubRepoUrl)
        secureSettings.putBoolean(PROFILE_VERIFIED_KEY, profile.isVerified)
    }
    
    override suspend fun getDeveloperProfile(): DeveloperProfile? {
        val id = secureSettings.getStringOrNull(PROFILE_ID_KEY) ?: return null
        return DeveloperProfile(
            id = id,
            githubUsername = secureSettings.getStringOrNull(PROFILE_USERNAME_KEY) ?: "",
            displayName = secureSettings.getStringOrNull(PROFILE_DISPLAY_NAME_KEY) ?: "",
            avatarUrl = secureSettings.getStringOrNull(PROFILE_AVATAR_URL_KEY) ?: "",
            bio = secureSettings.getStringOrNull(PROFILE_BIO_KEY) ?: "",
            email = secureSettings.getStringOrNull(PROFILE_EMAIL_KEY) ?: "",
            website = secureSettings.getStringOrNull(PROFILE_WEBSITE_KEY) ?: "",
            githubRepoUrl = secureSettings.getStringOrNull(PROFILE_REPO_URL_KEY) ?: "",
            isVerified = secureSettings.getBoolean(PROFILE_VERIFIED_KEY, false)
        )
    }
    
    override suspend fun logout() {
        secureSettings.clear()
    }
    
    companion object {
        private const val AUTH_TOKEN_KEY = "auth_token"
        private const val PROFILE_ID_KEY = "profile_id"
        private const val PROFILE_USERNAME_KEY = "profile_username"
        private const val PROFILE_DISPLAY_NAME_KEY = "profile_display_name"
        private const val PROFILE_AVATAR_URL_KEY = "profile_avatar_url"
        private const val PROFILE_BIO_KEY = "profile_bio"
        private const val PROFILE_EMAIL_KEY = "profile_email"
        private const val PROFILE_WEBSITE_KEY = "profile_website"
        private const val PROFILE_REPO_URL_KEY = "profile_repo_url"
        private const val PROFILE_VERIFIED_KEY = "profile_verified"
    }
} 