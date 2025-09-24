package com.andresruiz.qrcraft.create.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.andresruiz.qrcraft.core.domain.QRTypeExt.Companion.bgColor
import com.andresruiz.qrcraft.core.domain.QRTypeExt.Companion.color
import com.andresruiz.qrcraft.core.domain.QRTypeExt.Companion.icon
import com.andresruiz.qrcraft.core.domain.QRTypeExt.Companion.title
import com.andresruiz.qrcraft.core.domain.models.QRType
import com.andresruiz.qrcraft.core.presentation.design_system.surfaceHigher

@Composable
fun QROptionCell(
    qrType: QRType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceHigher)
            .clickable {
                onClick()
            }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(vertical = 20.dp, horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(100f))
                    .background(qrType.bgColor)
            ) {
                Icon(
                    painter = painterResource(qrType.icon),
                    tint = qrType.color,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = qrType.title.asString(),
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}