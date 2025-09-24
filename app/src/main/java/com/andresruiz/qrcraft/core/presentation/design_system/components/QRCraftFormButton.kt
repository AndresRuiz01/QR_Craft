package com.andresruiz.qrcraft.core.presentation.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.andresruiz.qrcraft.R
import com.andresruiz.qrcraft.core.presentation.design_system.onSurfaceDisabled

@Composable
fun QRCraftFormButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(100f))
            .background(if (isEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
            .clickable(
                enabled = isEnabled,
                onClick = onClick
            )
    ) {
        Text(
            text = stringResource(R.string.generate_qr),
            style = MaterialTheme.typography.labelLarge.copy(
                color = if (isEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceDisabled
            ),
        )
    }

}