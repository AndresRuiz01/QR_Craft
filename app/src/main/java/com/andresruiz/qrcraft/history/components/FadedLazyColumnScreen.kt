package com.andresruiz.qrcraft.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun FadedLazyColumnScreen(
    content: LazyListScope.() -> Unit
) {
    // The container Box holds both the list and the gradient overlay.
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // The scrollable list of items
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            content()

            item {
                Spacer(modifier = Modifier.height(150.dp))
            }

        }

        // The gradient overlay Box
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter) // Align to the bottom
                .fillMaxWidth()
                .height(200.dp) // Height of the fade
                .background(
                    brush = Brush.verticalGradient(
                        // The gradient starts transparent and fades to the background color
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.surface // Use your background color
                        )
                    )
                )
                // IMPORTANT: This makes the overlay ignore touch events
                .pointerInput(Unit) {}
        )
    }
}