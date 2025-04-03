package org.one.oneappstorebackend.navigation

/**
 * Routes used for navigation in the app.
 */
object Routes {
    // Login and registration
    const val Login = "login"
    
    // App list and details
    const val AppList = "app-list"
    const val AppCreate = "app-create"
    
    // Profile
    const val Profile = "profile"
    
    // App detail with ID parameter
    fun appDetail(appId: String) = "app-detail/$appId"
} 