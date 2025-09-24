package com.andresruiz.qrcraft.core.presentation.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import qrcode.internals.QRCodeSquare


@Composable
fun QRCode(
    data: Array<Array<QRCodeSquare>>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(Color.White)
    ) {
        data.forEach { row ->
            Row(
                modifier = Modifier.weight(1f)
            ) {
                row.forEach { item ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(if(item.dark) Color.Black else Color.White)
                    )
                }
            }
        }
    }
}