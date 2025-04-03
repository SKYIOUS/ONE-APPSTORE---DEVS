package org.one.oneappstorebackend.navigation

/**
 * Routes for navigation in the app.
 */
object Routes {
    const val LOGIN = "login"
    const val APP_LIST = "app_list"
    const val APP_DETAIL = "app_detail/{appId}"
    const val APP_CREATE = "app_create"
    const val APP_EDIT = "app_edit/{appId}"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
    
    /**
     * Creates a route for app detail.
     * @param appId The ID of the app.
     * @return The route string.
     */
    fun appDetail(appId: String) = "app_detail/$appId"
    
    /**
     * Creates a route for app edit.
     * @param appId The ID of the app.
     * @return The route string.
     */
    fun appEdit(appId: String) = "app_edit/$appId"
} 