package org.one.oneappstorebackend.auth

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import org.one.oneappstorebackend.service.GitHubService
import java.awt.Desktop
import java.net.URI
import java.util.Scanner
import java.util.concurrent.CompletableFuture

/**
 * Handles GitHub OAuth authentication on Desktop.
 */
class DesktopAuthHandler(private val githubService: GitHubService) : AuthHandler {
    
    // GitHub OAuth configuration
    private val clientId = "YOUR_GITHUB_CLIENT_ID" // Replace with your actual GitHub Client ID
    private val redirectUri = "http://localhost:8000/oauth/callback"
    private val scope = "user:email,read:user,repo"
    
    private val httpClient = HttpClient()
    
    override suspend fun authenticate(): String {
        return withContext(Dispatchers.IO) {
            try {
                // Build the authorization URL
                val authUrl = URLBuilder().apply {
                    protocol = URLProtocol.HTTPS
                    host = "github.com"
                    pathSegments = listOf("login", "oauth", "authorize")
                    parameters.append("client_id", clientId)
                    parameters.append("redirect_uri", redirectUri)
                    parameters.append("scope", scope)
                }.build()
                
                // Open the default browser for authentication
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(URI(authUrl.toString()))
                } else {
                    println("Please open this URL in your browser: $authUrl")
                }
                
                // Since we can't easily capture the OAuth callback in desktop, ask the user to paste the code
                println("After authorizing the application, GitHub will redirect you to a page with an authorization code.")
                println("Please paste the authorization code here and press Enter:")
                
                val scanner = Scanner(System.`in`)
                val code = scanner.nextLine().trim()
                
                // Exchange the code for a token
                val token = githubService.completeAuthentication(code)
                
                token ?: ""
            } catch (e: Exception) {
                println("Authentication error: ${e.message}")
                ""
            }
        }
    }
    
    @Serializable
    private data class TokenResponse(
        val access_token: String? = null,
        val token_type: String? = null,
        val scope: String? = null
    )
} 