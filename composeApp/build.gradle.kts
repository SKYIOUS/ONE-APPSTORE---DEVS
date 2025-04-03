import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version "1.9.0"
}

// Define version variables
val ktorVersion = "2.3.5"
val multiplatformSettingsVersion = "1.1.0"
val coroutinesVersion = "1.7.3"
val koinVersion = "3.5.0"

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm("desktop")
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
            runTask {
                outputFileName = "composeApp.js"
                devServer = devServer.copy(
                    port = 8080
                )
            }
        }
        binaries.executable()
    }
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            // Android specific network and storage
            implementation("io.ktor:ktor-client-android:2.3.5")
            implementation("androidx.security:security-crypto:1.1.0-alpha06")
            implementation("net.openid:appauth:0.11.1")
            // Browser dependency for Custom Tabs
            implementation("androidx.browser:browser:1.6.0")
            // Koin Android
            implementation("io.insert-koin:koin-android:3.5.0")
            implementation("io.insert-koin:koin-androidx-compose:3.5.0")
        }

        iosMain.dependencies {
            // iOS specific network
            implementation("io.ktor:ktor-client-darwin:2.3.5")
        }
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            
            // Ktor for networking
            implementation("io.ktor:ktor-client-core:$ktorVersion")
            implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
            implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
            
            // Kotlinx Serialization for JSON
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
            
            // Coroutines
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
            
            // Koin for dependency injection
            implementation("io.insert-koin:koin-core:$koinVersion")
            implementation("io.insert-koin:koin-compose:1.1.0")
            
            // DateTime library
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
            
            // Multiplatform Settings for storing preferences
            implementation("com.russhwolf:multiplatform-settings:$multiplatformSettingsVersion")
            implementation("com.russhwolf:multiplatform-settings-coroutines:$multiplatformSettingsVersion")
        }
        
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            // Desktop specific network
            implementation("io.ktor:ktor-client-java:$ktorVersion")
        }
        
        // Add WASM specific dependencies
        val wasmJsMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.ui)
                
                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation("com.russhwolf:multiplatform-settings:$multiplatformSettingsVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$coroutinesVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
            }
        }
        
        val androidMain by getting {
            dependencies {
                implementation("androidx.activity:activity-compose:1.8.0")
                implementation("androidx.appcompat:appcompat:1.6.1")
                implementation("androidx.core:core-ktx:1.12.0")
                implementation("io.ktor:ktor-client-android:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("androidx.browser:browser:1.5.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
                
                // Koin for Android
                implementation("io.insert-koin:koin-android:$koinVersion")
                implementation("io.insert-koin:koin-androidx-compose:$koinVersion")
            }
        }
    }
}

android {
    namespace = "org.one.oneappstorebackend"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.one.oneappstorebackend"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "org.one.oneappstorebackend.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.one.oneappstorebackend"
            packageVersion = "1.0.0"
        }
    }
}
