package org.one.oneappstorebackend

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ONE APPSTORE - DEVS",
    ) {
        App()
    }
}