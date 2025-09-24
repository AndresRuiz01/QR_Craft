package com.andresruiz.qrcraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.andresruiz.qrcraft.core.presentation.design_system.QRCraftTheme
import com.andresruiz.qrcraft.root.NavigationRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            QRCraftTheme {
                NavigationRoot(
                    closeApplication = {
                        finishAndRemoveTask()
                    }
                )
            }
        }
    }
}