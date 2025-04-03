import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.core.context.startKoin
import org.one.oneappstorebackend.App
import org.one.oneappstorebackend.di.DesktopPlatformFactory
import org.one.oneappstorebackend.di.commonModule

fun main() {
    // Initialize Koin DI
    startKoin {
        modules(
            commonModule(),
            DesktopPlatformFactory.createPlatformModule()
        )
    }
    
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "ONE App Store"
        ) {
            App()
        }
    }
} 