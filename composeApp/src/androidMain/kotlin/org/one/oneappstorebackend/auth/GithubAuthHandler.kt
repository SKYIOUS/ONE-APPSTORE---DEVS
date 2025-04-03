package org.one.oneappstorebackend.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import org.one.oneappstorebackend.config.OAuthConfig
import org.one.oneappstorebackend.service.GitHubServiceImpl
import kotlin.coroutines.resume

/**
 * Handles GitHub OAuth authentication on Android.
 */
class GithubAuthHandler(
    private val context: Context,
    private val gitHubService: GitHubServiceImpl
) {
    private val TAG = "GithubAuthHandler"
    
    // Create the authorization service configuration
    private val serviceConfig = AuthorizationServiceConfiguration(
        Uri.parse(OAuthConfig.GITHUB_AUTHORIZE_URL),
        Uri.parse(OAuthConfig.GITHUB_TOKEN_URL)
    )
    
    // Create the authorization service
    private val authService = AuthorizationService(context)
    
    /**
     * Starts the GitHub OAuth flow by opening the browser for authorization.
     * @return The authorization code if successful, null otherwise.
     */
    suspend fun startAuthFlow(): String? = withContext(Dispatchers.IO) {
        return@withContext suspendCancellableCoroutine { continuation ->
            try {
                // Create the authorization request
                val authRequest = AuthorizationRequest.Builder(
                    serviceConfig,
                    OAuthConfig.GITHUB_CLIENT_ID,
                    ResponseTypeValues.CODE,
                    Uri.parse("${OAuthConfig.CALLBACK_SCHEME}://${OAuthConfig.CALLBACK_HOST}")
                ).setScopes(OAuthConfig.SCOPES.split(" "))
                    .build()
                
                // Create the custom tabs intent
                val customTabsIntent = CustomTabsIntent.Builder().build()
                
                // Get the authorization request intent
                val authIntent = authService.getAuthorizationRequestIntent(
                    authRequest,
                    customTabsIntent
                )
                
                // Store the continuation to resume when we receive the callback
                pendingContinuation = continuation
                
                // Start the authorization activity
                context.startActivity(authIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                
                // Cancel the authorization service when the coroutine is cancelled
                continuation.invokeOnCancellation {
                    authService.dispose()
                    pendingContinuation = null
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error starting auth flow", e)
                continuation.resume(null)
            }
        }
    }
    
    /**
     * Handles the authorization response from the browser.
     * @param intent The intent received from the browser.
     * @return true if the response was handled, false otherwise.
     */
    suspend fun handleAuthResponse(intent: Intent): Boolean {
        val uri = intent.data ?: return false
        
        if (uri.scheme == OAuthConfig.CALLBACK_SCHEME && 
            uri.host == OAuthConfig.CALLBACK_HOST) {
            
            // Extract the code from the URI
            val code = uri.getQueryParameter("code")
            if (code != null) {
                // Exchange the code for a token
                val token = gitHubService.completeAuthentication(code)
                
                // Resume the coroutine waiting for the auth flow to complete
                pendingContinuation?.resume(code)
                pendingContinuation = null
                
                return true
            }
        }
        
        return false
    }
    
    companion object {
        // Store the continuation to resume when we receive the callback
        private var pendingContinuation: kotlin.coroutines.Continuation<String?>? = null
    }
} 