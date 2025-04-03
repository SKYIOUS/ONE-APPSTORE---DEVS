package org.one.oneappstorebackend.model

/**
 * Represents metadata for an application in the app store.
 */
data class AppMetadata(
    val id: String,
    val name: String,
    val description: String,
    val shortDescription: String,
    val version: String,
    val developerId: String,
    val category: String,
    val tags: List<String> = emptyList(),
    val iconUrl: String = "",
    val screenshotUrls: List<String> = emptyList(),
    val websiteUrl: String = "",
    val privacyPolicyUrl: String = "",
    val supportEmail: String = "",
    val releaseNotes: String = "",
    val releaseDate: String = "",
    val minSdkVersion: Map<String, String> = emptyMap(),
    val platforms: List<AppPlatform> = emptyList()
)

/**
 * Represents platform-specific information for an app.
 */
data class AppPlatform(
    val type: PlatformType,
    val packageUrl: String = "",
    val packageSize: Long = 0,
    val installInstructions: String = ""
)

/**
 * Enum representing different platforms supported by the app store.
 */
enum class PlatformType {
    ANDROID,
    IOS,
    WINDOWS,
    LINUX,
    MACOS,
    WEB
}

/**
 * Enum representing different release channels.
 */
enum class ReleaseChannel {
    STABLE,
    BETA
} 