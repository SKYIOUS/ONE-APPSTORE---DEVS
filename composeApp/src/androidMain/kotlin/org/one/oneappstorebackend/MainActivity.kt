package org.one.oneappstorebackend

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.one.oneappstorebackend.auth.GithubAuthHandler
import org.one.oneappstorebackend.di.androidAppContext
import org.one.oneappstorebackend.service.GitHubServiceImpl
import org.one.oneappstorebackend.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModel()
    private lateinit var githubAuthHandler: GithubAuthHandler
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Set the app context for dependency injection
        androidAppContext = applicationContext
        
        // Initialize the GitHub auth handler
        val gitHubService = authViewModel.getGitHubService() as GitHubServiceImpl
        githubAuthHandler = GithubAuthHandler(applicationContext, gitHubService)
        
        // Set the auth handler in the view model
        authViewModel.setAuthHandler(githubAuthHandler)
        
        // Handle the intent if it's from OAuth callback
        handleIntent(intent)
        
        setContent {
            App()
        }
    }
    
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleIntent(it) }
    }
    
    private fun handleIntent(intent: Intent) {
        // Check if this is a callback from the OAuth flow
        if (intent.data != null) {
            lifecycleScope.launch {
                githubAuthHandler.handleAuthResponse(intent)
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}