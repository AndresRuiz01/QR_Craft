package com.andresruiz.qrcraft.core.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun ChangeStatusBarColors(darkIcons: Boolean) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            // Get the window from the view's context
            val window = (view.context as android.app.Activity).window
            // Create an insets controller
            val insetsController = WindowCompat.getInsetsController(window, view)
            // Set the status bar icons to be light or dark
            insetsController.isAppearanceLightStatusBars = darkIcons
        }
    }
}