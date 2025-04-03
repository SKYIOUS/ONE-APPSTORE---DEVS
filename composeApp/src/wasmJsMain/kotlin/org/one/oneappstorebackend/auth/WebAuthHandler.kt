package org.one.oneappstorebackend.auth

import kotlinx.browser.window
import kotlinx.coroutines.suspendCancellableCoroutine
import org.one.oneappstorebackend.service.GitHubService
import kotlin.coroutines.resume
import kotlin.js.Promise

class WebAuthHandler(private val githubService: GitHubService) : AuthHandler {
    
    // GitHub OAuth configuration
    private val clientId = "YOUR_GITHUB_CLIENT_ID" // Replace with your actual GitHub Client ID
    private val redirectUri = window.location.origin + "/auth/callback"
    private val scope = "user:email,read:user,repo"
    
    override suspend fun authenticate(): String {
        return suspendCancellableCoroutine { continuation ->
            // Open a popup window for GitHub OAuth
            val authUrl = "https://github.com/login/oauth/authorize" +
                    "?client_id=$clientId" +
                    "&redirect_uri=$redirectUri" +
                    "&scope=$scope"
            
            val width = 600
            val height = 700
            val left = (window.screen.width - width) / 2
            val top = (window.screen.height - height) / 2
            
            val popup = window.open(
                authUrl,
                "GitHub OAuth",
                "width=$width,height=$height,left=$left,top=$top"
            )
            
            // Function to handle the OAuth callback
            val handleCallback = { code: String ->
                // Exchange the code for an access token
                exchangeCodeForToken(code)
                    .then { token ->
                        continuation.resume(token)
                        cleanup()
                    }
                    .catch { error ->
                        console.error("Authentication error: ", error)
                        continuation.resume("")
                        cleanup()
                    }
            }
            
            // Listen for messages from the popup
            val messageListener: (dynamic) -> Unit = { event ->
                if (event.origin == window.location.origin && event.data.type == "githubOAuth") {
                    handleCallback(event.data.code as String)
                }
            }
            
            window.addEventListener("message", messageListener)
            
            // Function to clean up event listeners
            fun cleanup() {
                window.removeEventListener("message", messageListener)
                if (popup != null && !popup.closed) {
                    popup.close()
                }
            }
            
            // Set a timer to close the popup if authentication takes too long
            val timeoutId = window.setTimeout({
                continuation.resume("")
                cleanup()
            }, 300000) // 5 minutes timeout
            
            continuation.invokeOnCancellation {
                window.clearTimeout(timeoutId)
                cleanup()
            }
        }
    }
    
    private fun exchangeCodeForToken(code: String): Promise<String> {
        // In a real implementation, you would make a server call to exchange the code for a token
        // For security reasons, this should be done on your server, not in the client
        
        // This is a mock implementation - in a real app, call your backend API
        return Promise { resolve, reject ->
            // Call your server endpoint that handles the OAuth code exchange
            // Example:
            // fetch("/api/auth/github/token?code=$code")
            //     .then { response -> response.text() }
            //     .then { token -> resolve(token) }
            //     .catch { error -> reject(error) }
            
            // For now, just mock the response
            resolve("mock_github_token")
        }
    }
} 