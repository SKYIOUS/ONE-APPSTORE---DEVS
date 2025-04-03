# Multiplatform App Store - Developer App

This is the Developer Portal application for the Multiplatform App Store project. It allows developers to publish and manage their applications across multiple platforms using GitHub as a backend storage system.

## Features

- **GitHub OAuth Authentication**: Securely login with your GitHub account
- **App Management**: Create, update, and publish applications
- **Multi-Platform Support**: Publish applications for Android, iOS, Windows, Linux, macOS, and Web
- **Release Channels**: Manage stable and beta releases of your applications
- **Package Management**: Upload different package formats for different platforms
- **Metadata Management**: Comprehensive metadata editing for app store listings

## Architecture

This app is built with Kotlin Multiplatform, targeting:
- Android (Native app)
- iOS (Native app)
- Desktop (Windows & Linux)
- Web

The backend uses GitHub repositories and GitHub API for storage and authentication:
- Each developer has their own repository (submodule)
- Apps are organized in a structured directory hierarchy
- Git LFS is used for large binary files
- GitHub Releases for versioning

## Technical Stack

- **UI**: Compose Multiplatform
- **Networking**: Ktor
- **Serialization**: Kotlinx Serialization
- **Dependency Injection**: Koin
- **Authentication**: GitHub OAuth
- **Storage**: GitHub API

## Getting Started

### Prerequisites

- Android Studio Arctic Fox or newer
- Xcode 13+ (for iOS builds)
- JDK 11+
- GitHub account

### Building the Project

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Run on your preferred platform

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

This is a Kotlin Multiplatform project targeting Android, iOS, Web, Desktop.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that's common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple's CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you're sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/)…

We would appreciate your feedback on Compose/Web and Kotlin/Wasm in the public Slack channel [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web).
If you face any issues, please report them on [GitHub](https://github.com/JetBrains/compose-multiplatform/issues).

You can open the web application by running the `:composeApp:wasmJsBrowserDevelopmentRun` Gradle task.