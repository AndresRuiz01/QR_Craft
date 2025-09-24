package com.andresruiz.qrcraft.core.presentation.design_system

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val ColorScheme.success: Color
    get() = Color(0xFF4DDA9D)

val ColorScheme.surfaceHigher: Color
    get() = Color(0xFFFFFFFF)

val ColorScheme.onSurfaceAlt: Color
    get() = Color(0xFF505F6A)

val ColorScheme.onSurfaceDisabled: Color
    get() = Color(0xFF8C99A2)

val ColorScheme.onOverlay: Color
    get() = Color(0xFFFFFFFF)

val ColorScheme.qrText: Color
    get() = Color(0xFF583DC5)

val ColorScheme.qrTextBg: Color
    get() = Color(0xFF583DC5).copy(alpha = 0.1f)

val ColorScheme.qrContact: Color
    get() = Color(0xFF259570)

val ColorScheme.qrContactBg: Color
    get() = Color(0xFF259570).copy(alpha = 0.1f)

val ColorScheme.qrGeo: Color
    get() = Color(0xFFB51D5C)

val ColorScheme.qrGeoBg: Color
    get() = Color(0xFFB51D5C).copy(alpha = 0.1f)

val ColorScheme.qrLink: Color
    get() = Color(0xFF373F05)

val ColorScheme.qrLinkBg: Color
    get() = Color(0xFFEBFF69).copy(alpha = 0.3f)

val ColorScheme.qrPhone: Color
    get() = Color(0xFFC86017)

val ColorScheme.qrPhoneBg: Color
    get() = Color(0xFFC86017).copy(alpha = 0.3f)

val ColorScheme.qrWifi: Color
    get() = Color(0xFF1F44CD)

val ColorScheme.qrWifiBg: Color
    get() = Color(0xFF1F44CD).copy(alpha = 0.3f)

private val ColorScheme = lightColorScheme(
    primary = Color(0xFFEBFF69),
    surface = Color(0xFFEDF2F5),
    onSurface = Color(0xFF273037),
    error = Color(0xFFF12244),
)

@Composable
fun QRCraftTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
val MyRippleConfiguration =
    RippleConfiguration(color = Color(0xFFEBFF69), rippleAlpha = RippleAlpha(0f,0f,0f,0.3f))