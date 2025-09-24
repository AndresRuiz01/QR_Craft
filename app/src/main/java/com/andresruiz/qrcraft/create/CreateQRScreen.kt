package com.andresruiz.qrcraft.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.andresruiz.qrcraft.core.domain.models.QRType
import com.andresruiz.qrcraft.core.presentation.design_system.components.QRCraftNavigationBar
import com.andresruiz.qrcraft.core.presentation.ui.ChangeStatusBarColors
import com.andresruiz.qrcraft.create.components.QROptionCell

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQRScreen(
    navigateToScan: () -> Unit,
    navigateToHistory: () -> Unit,
    navigateToDetails: (QRType) -> Unit,
) {

    ChangeStatusBarColors(true)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        floatingActionButton = {
            QRCraftNavigationBar(
                navigateToHistory = navigateToHistory,
                navigateToCreate = {}, // empty since we are on this screen
                navigateToScan = navigateToScan,
                onCreateTab = true,
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create QR", // TODO: Change to string resource
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .wrapContentWidth()
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .padding(innerPadding)
        ) {
            items(
                items = QRType.entries.toTypedArray()
            ) { qrType ->
                QROptionCell(
                    qrType = qrType,
                    onClick = {
                        navigateToDetails(qrType)
                    }
                )
            }
        }
    }
}