package org.one.oneappstorebackend.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.browser.customtabs.CustomTabsIntent
import kotlinx.coroutines.suspendCancellableCoroutine
import org.one.oneappstorebackend.service.GitHubService
import kotlin.coroutines.resume

class AndroidAuthHandler(
    private val context: Context,
    private val activity: ComponentActivity?,
    private val githubService: GitHubService
) : AuthHandler {
    
    // GitHub OAuth configuration
    private val clientId = "YOUR_GITHUB_CLIENT_ID" // Replace with your actual GitHub Client ID
    private val clientSecret = "YOUR_GITHUB_CLIENT_SECRET" // Replace with your actual GitHub Client Secret
    private val redirectUri = "oneappstore://callback"
    private val scope = "user:email,read:user,repo"
    
    // Used to store the continuation for the OAuth flow
    private var authContinuation: (suspend (String) -> Unit)? = null
    
    // Activity result launcher for browser
    private var browserLauncher: ActivityResultLauncher<Intent>? = null
    
    init {
        activity?.let { act ->
            browserLauncher = act.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                // This will be triggered when the browser is closed without completing OAuth
                // We can check if the auth was completed via deep link instead
                if (authContinuation != null) {
                    // If we still have an active continuation, it means OAuth wasn't completed
                    authContinuation?.let { it as suspend (String) -> Unit }?.let {
                        authContinuation = null
                        it("")
                    }
                }
            }
        }
    }
    
    /**
     * Process the deep link URI that comes back from GitHub OAuth.
     * This should be called from your activity's onNewIntent method.
     */
    fun handleDeepLink(uri: Uri) {
        if (uri.toString().startsWith(redirectUri)) {
            val code = uri.getQueryParameter("code")
            if (code != null) {
                authContinuation?.let { it as suspend (String) -> Unit }?.let {
                    authContinuation = null
                    it(code)
                }
            } else {
                // Handle error
                authContinuation?.let { it as suspend (String) -> Unit }?.let {
                    authContinuation = null
                    it("")
                }
            }
        }
    }
    
    override suspend fun authenticate(): String {
        return suspendCancellableCoroutine { continuation ->
            val authUrl = Uri.parse("https://github.com/login/oauth/authorize")
                .buildUpon()
                .appendQueryParameter("client_id", clientId)
                .appendQueryParameter("redirect_uri", redirectUri)
                .appendQueryParameter("scope", scope)
                .build()
            
            // Store the continuation to be called when OAuth completes
            authContinuation = { code ->
                if (code.isNotEmpty()) {
                    // Exchange the code for a token
                    val token = githubService.completeAuthentication(code)
                    continuation.resume(token ?: "")
                } else {
                    continuation.resume("")
                }
            }
            
            // Launch the browser with Custom Tabs if possible
            try {
                val customTabsIntent = CustomTabsIntent.Builder().build()
                
                if (activity != null && browserLauncher != null) {
                    // Use activity result API if available
                    val intent = customTabsIntent.intent.apply {
                        data = authUrl
                    }
                    browserLauncher?.launch(intent)
                } else {
                    // Fallback to just launching the intent directly
                    customTabsIntent.intent.data = authUrl
                    customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(customTabsIntent.intent)
                }
            } catch (e: Exception) {
                // Fallback to regular browser intent
                val intent = Intent(Intent.ACTION_VIEW, authUrl).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
            
            // If the coroutine is cancelled, clean up
            continuation.invokeOnCancellation {
                authContinuation = null
            }
        }
    }
    
    companion object {
        // Registry to keep track of the active handler for deep link processing
        private var INSTANCE: AndroidAuthHandler? = null
        
        fun getInstance(): AndroidAuthHandler? = INSTANCE
        
        fun register(handler: AndroidAuthHandler) {
            INSTANCE = handler
        }
        
        fun unregister(handler: AndroidAuthHandler) {
            if (INSTANCE == handler) {
                INSTANCE = null
            }
        }
    }
} 