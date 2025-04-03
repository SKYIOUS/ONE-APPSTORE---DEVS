package org.one.oneappstorebackend.model

/**
 * Represents a developer profile in the app store.
 */
data class DeveloperProfile(
    val id: String,
    val githubUsername: String,
    val displayName: String,
    val avatarUrl: String,
    val bio: String = "",
    val email: String = "",
    val website: String = "",
    val githubRepoUrl: String = "",
    val isVerified: Boolean = false
) 