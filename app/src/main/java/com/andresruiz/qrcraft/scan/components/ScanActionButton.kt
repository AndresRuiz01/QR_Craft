package com.andresruiz.qrcraft.scan.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.andresruiz.qrcraft.core.presentation.design_system.surfaceHigher

@Composable
fun ScanActionButton(
    @DrawableRes enabledIcon: Int,
    @DrawableRes disabledIcon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(
                if (enabled) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surfaceHigher
                }
            )
            .clickable {
                onClick()
            }
    ) {
        Icon(
            painter = painterResource(id = if (enabled) enabledIcon else disabledIcon),
            contentDescription = null,
        )
    }
}