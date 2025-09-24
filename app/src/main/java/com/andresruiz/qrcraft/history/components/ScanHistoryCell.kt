package com.andresruiz.qrcraft.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andresruiz.qrcraft.R
import com.andresruiz.qrcraft.core.domain.QRTypeExt.Companion.bgColor
import com.andresruiz.qrcraft.core.domain.QRTypeExt.Companion.color
import com.andresruiz.qrcraft.core.domain.QRTypeExt.Companion.icon
import com.andresruiz.qrcraft.core.domain.QRTypeExt.Companion.title
import com.andresruiz.qrcraft.core.domain.models.QR
import com.andresruiz.qrcraft.core.presentation.design_system.onSurfaceDisabled
import com.andresruiz.qrcraft.core.presentation.design_system.surfaceHigher

@Composable
fun ScanHistoryCell(
    qr: QR,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .background(MaterialTheme.colorScheme.surfaceHigher)
            .combinedClickable(
                onClick = {
                    onClick()
                },
                onLongClick = {
                    onLongClick()
                }
            )
            .padding(12.dp)
    ) {
        // Icon
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(100f))
                .background(qr.qrType.bgColor)
        ) {
            Icon(
                painter = painterResource(qr.qrType.icon),
                tint = qr.qrType.color,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }

        // Title / Content / Date
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = qr.title.ifEmpty { qr.qrType.title.asString() },
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                text = qr.displayString,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = qr.displayDate,
                style = TextStyle(
                    fontWeight = FontWeight.W400,
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceDisabled
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(32.dp)
                .offset(
                    x = 4.dp,
                    y = (-4).dp
                )
                .clickable(
                    interactionSource = null,
                    indication = null
                ) {
                    onFavoriteClick()
                }
        ) {
            Icon(
                painter = painterResource(if(qr.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }

    }
}