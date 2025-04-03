package org.one.oneappstorebackend.config

/**
 * Configuration for GitHub OAuth.
 * Note: In a real application, these values should not be hardcoded but retrieved securely.
 */
object OAuthConfig {
    // Replace these with your actual GitHub OAuth app credentials
    const val GITHUB_CLIENT_ID = "Ov23liAP64oZRebzypXs"
    const val GITHUB_CLIENT_SECRET = "87055a414362088a9001322e6397944e887ec233"
    
    // OAuth endpoints
    const val GITHUB_AUTHORIZE_URL = "https://github.com/login/oauth/authorize"
    const val GITHUB_TOKEN_URL = "https://github.com/login/oauth/access_token"
    const val GITHUB_USER_API_URL = "https://api.github.com/user"
    
    // Callback URLs (needs to be configured in your GitHub OAuth app)
    // These are platform-specific and will be implemented differently for each platform
    const val CALLBACK_SCHEME = "oneappstore"
    const val CALLBACK_HOST = "oauth-callback"

    // Scopes for GitHub OAuth
    // These define what permissions we're requesting from GitHub
    val SCOPES = listOf(
        "repo",          // Access to private repositories
        "user:email",    // Access to user's email
        "workflow"       // Access to workflows (for creating releases)
    ).joinToString(" ")
} 