package org.one.oneappstorebackend.di

import org.koin.dsl.module
import org.one.oneappstorebackend.repository.AppRepository
import org.one.oneappstorebackend.repository.AuthRepository
import org.one.oneappstorebackend.service.GitHubService
import org.one.oneappstorebackend.viewmodel.AppEditViewModel
import org.one.oneappstorebackend.viewmodel.AppListViewModel
import org.one.oneappstorebackend.viewmodel.AuthViewModel

/**
 * Koin module for dependency injection.
 */
val appModule = module {
    // ViewModels
    factory { AuthViewModel(get(), get()) }
    factory { AppListViewModel(get()) }
    factory { AppEditViewModel(get()) }
    
    // Repositories
    single<AuthRepository> { PlatformAuthRepository(get()) }
    single<AppRepository> { GitHubAppRepository(get()) }
    
    // Services
    single<GitHubService> { GitHubServiceImpl(get()) }
    
    // Platform specific dependencies
    single { createHttpClient() }
    single { createSettings() }
}

/**
 * Platform specific implementation will be provided by each platform.
 */
expect fun createHttpClient(): Any

/**
 * Platform specific implementation will be provided by each platform.
 */
expect fun createSettings(): Any

/**
 * Platform specific implementation of AuthRepository.
 */
expect class PlatformAuthRepository(settings: Any) : AuthRepository

/**
 * Implementation of AppRepository using GitHub.
 */
class GitHubAppRepository(private val gitHubService: GitHubService) : AppRepository {
    // Implementation will delegate to GitHubService
    override suspend fun getDeveloperApps(developerId: String) = 
        gitHubService.getDeveloperApps(developerId)
    
    override suspend fun getAppById(developerId: String, appId: String): org.one.oneappstorebackend.model.AppMetadata? {
        return getDeveloperApps(developerId).firstOrNull { it.id == appId }
    }
    
    override suspend fun createApp(
        developerId: String, 
        appMetadata: org.one.oneappstorebackend.model.AppMetadata, 
        channel: org.one.oneappstorebackend.model.ReleaseChannel
    ) = gitHubService.uploadAppMetadata(developerId, appMetadata, channel)
    
    override suspend fun updateApp(
        developerId: String, 
        appMetadata: org.one.oneappstorebackend.model.AppMetadata, 
        channel: org.one.oneappstorebackend.model.ReleaseChannel
    ) = gitHubService.uploadAppMetadata(developerId, appMetadata, channel)
    
    override suspend fun uploadAppPackage(
        developerId: String, 
        appId: String, 
        platform: String, 
        fileName: String, 
        fileBytes: ByteArray,
        channel: org.one.oneappstorebackend.model.ReleaseChannel
    ) = gitHubService.uploadAppPackage(developerId, appId, platform, fileName, fileBytes, channel)
    
    override suspend fun createRelease(
        developerId: String, 
        appId: String, 
        version: String, 
        releaseNotes: String,
        channel: org.one.oneappstorebackend.model.ReleaseChannel
    ) = gitHubService.createRelease(developerId, appId, version, releaseNotes, channel)
    
    override suspend fun deleteApp(developerId: String, appId: String): Boolean {
        // TODO: Implement app deletion through GitHubService
        return false
    }
} 