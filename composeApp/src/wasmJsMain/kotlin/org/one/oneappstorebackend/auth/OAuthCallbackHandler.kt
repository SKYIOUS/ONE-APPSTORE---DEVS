package org.one.oneappstorebackend.auth

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.dom.clear
import org.w3c.dom.url.URLSearchParams

/**
 * Handler for OAuth callbacks in the web application.
 * This class processes GitHub OAuth callbacks and communicates the result back to the main window.
 */
object OAuthCallbackHandler {
    
    /**
     * Initialize the callback handler. This should be called when the application starts.
     */
    fun initialize() {
        // Check if we're on the callback page
        if (window.location.pathname == "/auth/callback") {
            handleOAuthCallback()
        }
    }
    
    /**
     * Process the OAuth callback from GitHub.
     */
    private fun handleOAuthCallback() {
        val urlParams = URLSearchParams(window.location.search)
        val code = urlParams.get("code")
        
        if (code != null) {
            // Send the code back to the opener window
            window.opener?.postMessage(
                obj { 
                    this.type = "githubOAuth"
                    this.code = code
                },
                window.location.origin
            )
            
            // Display a success message
            displayMessage("Authentication successful! You can close this window.")
        } else {
            // Check for error
            val error = urlParams.get("error")
            val errorDescription = urlParams.get("error_description")
            
            // Display an error message
            val message = if (error != null) {
                "Authentication error: $error ${errorDescription?.let { "- $it" } ?: ""}"
            } else {
                "Authentication failed with an unknown error."
            }
            
            displayMessage(message)
        }
    }
    
    /**
     * Display a message on the callback page.
     */
    private fun displayMessage(message: String) {
        // Clear the page content
        document.body?.clear()
        
        // Create and add a message element
        val messageElement = document.createElement("div").apply {
            textContent = message
            setAttribute("style", "font-family: sans-serif; text-align: center; margin-top: 100px; font-size: 18px;")
        }
        
        document.body?.appendChild(messageElement)
        
        // Auto-close after a delay
        window.setTimeout({
            window.close()
        }, 3000) // Close after 3 seconds
    }
}

/**
 * Helper function to create JavaScript objects with dynamic properties.
 */
private inline fun obj(init: dynamic.() -> Unit): dynamic {
    val obj = js("{}")
    init(obj)
    return obj
} 