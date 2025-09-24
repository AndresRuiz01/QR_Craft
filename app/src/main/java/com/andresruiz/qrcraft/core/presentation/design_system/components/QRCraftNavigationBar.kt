package com.andresruiz.qrcraft.core.presentation.design_system.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andresruiz.qrcraft.R
import com.andresruiz.qrcraft.core.presentation.design_system.MyRippleConfiguration
import com.andresruiz.qrcraft.core.presentation.design_system.QRCraftTheme
import com.andresruiz.qrcraft.core.presentation.design_system.qrLinkBg
import com.andresruiz.qrcraft.core.presentation.design_system.surfaceHigher

@Composable
fun QRCraftNavigationBar(
    navigateToScan: () -> Unit,
    navigateToHistory: () -> Unit,
    navigateToCreate: () -> Unit,
    modifier: Modifier = Modifier,
    onCreateTab: Boolean = false,
    onHistoryTab: Boolean = false,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(64.dp)
            .width(168.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .width(168.dp)
                .height(52.dp)
                .clip(RoundedCornerShape(100))
                .background(MaterialTheme.colorScheme.surfaceHigher)
        ) {
            NavigationItem(
                icon = R.drawable.ic_history,
                contentDescription = "History",
                onClick = navigateToHistory,
                onTab = onHistoryTab
            )

            NavigationItem(
                icon = R.drawable.ic_create,
                contentDescription = "Create",
                onClick = navigateToCreate,
                onTab = onCreateTab
            )

        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(100))
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                    navigateToScan()
                }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_qr),
                contentDescription = "QR",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NavigationItem(
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    onTab: Boolean = false,
    contentDescription: String? = null,
) {
    CompositionLocalProvider(LocalRippleConfiguration provides MyRippleConfiguration) {
        IconButton (
            onClick = onClick,
            modifier = Modifier.padding(horizontal = 4.dp),
            colors = IconButtonDefaults.iconButtonColors().copy(
                containerColor = if (onTab) MaterialTheme.colorScheme.qrLinkBg else MaterialTheme.colorScheme.surfaceHigher
            )
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Preview
@Composable
private fun NavigationBarPreview() {
    QRCraftTheme {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
        ) {
            QRCraftNavigationBar(
                navigateToHistory = {},
                navigateToCreate = {},
                navigateToScan = {},
                modifier = Modifier
            )
        }
    }
}