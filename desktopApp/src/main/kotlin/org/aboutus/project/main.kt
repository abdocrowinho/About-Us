package org.aboutus.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.aboutus.project.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "About Us",
    ) {
        App()
    }
}
