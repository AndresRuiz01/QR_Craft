package com.andresruiz.qrcraft.history.components

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.andresruiz.qrcraft.R
import com.andresruiz.qrcraft.core.presentation.design_system.surfaceHigher
import com.andresruiz.qrcraft.history.HistoryAction
import com.andresruiz.qrcraft.history.HistoryState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryModal(
    state: HistoryState,
    onAction: (HistoryAction) -> Unit,
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceHigher,
        dragHandle = {},
        onDismissRequest = {
            onAction(HistoryAction.DismissModal)
        },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            ModalRow(
                icon = R.drawable.ic_share,
                text = "Share",
                onClick = {
                    val qr = (state.scannedQRs + state.generatedQRs).find { it.id == state.currentQR }
                    qr?.let {
                        // Create an Intent with the ACTION_SEND action
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, it.displayString)
                        }

                        // Create a chooser for the user to select an app
                        val shareIntent = Intent.createChooser(sendIntent, null)

                        // Launch the intent
                        context.startActivity(shareIntent)
                    }
                }
            )
            ModalRow(
                icon = R.drawable.ic_trash,
                text = "Delete",
                color = MaterialTheme.colorScheme.error,
                onClick = {
                    onAction(HistoryAction.DeleteQR)
                }
            )
        }
    }
}