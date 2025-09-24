package com.andresruiz.qrcraft.scan.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.andresruiz.qrcraft.R
import com.andresruiz.qrcraft.core.presentation.design_system.onOverlay

@Composable
fun CutoutOverlay(
    cutoutSize: Dp,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val cutoutSizeAsPx = cutoutSize.toPx()
        val cornerRadiusPx = 24.dp.toPx()

        drawRect(
            color = Color.Black.copy(alpha = 0.5f),
        )

        val topLeftX = (canvasWidth - cutoutSizeAsPx) / 2
        val topLeftY = (canvasHeight - cutoutSizeAsPx) / 2

        // This is the magic part: we use BlendMode.Clear to erase a section of the canvas
        drawRoundRect(
            topLeft = Offset(topLeftX, topLeftY),
            size = Size(cutoutSizeAsPx, cutoutSizeAsPx),
            cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
            color = Color.Transparent, // The color doesn't matter with BlendMode.Clear
            blendMode = BlendMode.Clear
        )

        // Draw corner frames
        val frameStrokeWidth = 4.dp.toPx()
        val frameLength = 32.dp.toPx()
        val topRightX = topLeftX + cutoutSizeAsPx
        val bottomRightY = topLeftY + cutoutSizeAsPx

        val path = Path().apply {
            // Top-left corner
            moveTo(topLeftX, topLeftY + frameLength)
            lineTo(topLeftX, topLeftY + cornerRadiusPx)
            quadraticTo(
                x1 = topLeftX, y1 = topLeftY,
                x2 = topLeftX + cornerRadiusPx, y2 = topLeftY
            )
            lineTo(topLeftX + frameLength, topLeftY)

            // Top-right corner
            moveTo(topRightX - frameLength, topLeftY)
            lineTo(topRightX - cornerRadiusPx, topLeftY)
            quadraticTo(
                x1 = topRightX, y1 = topLeftY,
                x2 = topRightX, y2 = topLeftY + cornerRadiusPx
            )
            lineTo(topRightX, topLeftY + frameLength)

            // Bottom-right corner
            moveTo(topRightX, bottomRightY - frameLength)
            lineTo(topRightX, bottomRightY - cornerRadiusPx)
            quadraticTo(
                x1 = topRightX, y1 = bottomRightY,
                x2 = topRightX - cornerRadiusPx, y2 = bottomRightY
            )
            lineTo(topRightX - frameLength, bottomRightY)

            // Bottom-left corner
            moveTo(topLeftX + frameLength, bottomRightY)
            lineTo(topLeftX + cornerRadiusPx, bottomRightY)
            quadraticTo(
                x1 = topLeftX, y1 = bottomRightY,
                x2 = topLeftX, y2 = bottomRightY - cornerRadiusPx
            )
            lineTo(topLeftX, bottomRightY - frameLength)
        }

        drawPath(
            path = path,
            color = primaryColor,
            style = Stroke(width = frameStrokeWidth, cap = StrokeCap.Round),
        )
    }
}

@Composable
fun LoadingCutoutOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onOverlay,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = stringResource(R.string.loading),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color =  MaterialTheme.colorScheme.onOverlay,
                )
            )
        }
    }
}