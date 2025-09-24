package com.andresruiz.qrcraft.scan

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.andresruiz.qrcraft.R
import com.andresruiz.qrcraft.core.presentation.design_system.components.QRCraftNavigationBar
import com.andresruiz.qrcraft.core.presentation.design_system.onOverlay
import com.andresruiz.qrcraft.core.presentation.ui.ChangeStatusBarColors
import com.andresruiz.qrcraft.core.presentation.ui.ObserveAsEvents
import com.andresruiz.qrcraft.scan.components.CameraPreview
import com.andresruiz.qrcraft.scan.components.CutoutOverlay
import com.andresruiz.qrcraft.scan.components.ScanActionButton
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun ScanScreenRoot(
    navigateToCreate: () -> Unit,
    navigateToHistory: () -> Unit,
    navigateToScanResult: (Long) -> Unit,
    viewModel: ScanScreenViewModel = koinViewModel<ScanScreenViewModel>(),
) {
    ChangeStatusBarColors(false)

    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is ScanEvent.OnQRScanned -> {
                navigateToScanResult(event.id)
            }
        }
    }

    ScanScreen(
        navigateToCreate = navigateToCreate,
        navigateToHistory = navigateToHistory,
        onAction = viewModel::onAction,
    )

}

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScanScreen(
    navigateToCreate: () -> Unit,
    navigateToHistory: () -> Unit,
    onAction: (ScanAction) -> Unit,
) {
    var isTorchEnabled: Boolean by remember { mutableStateOf(false) }

    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val cutoutSize = 320.dp
    Scaffold(
        floatingActionButton = {
            QRCraftNavigationBar(
                navigateToHistory = navigateToHistory,
                navigateToCreate = navigateToCreate,
                navigateToScan = {} // empty since we are already here
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        modifier = Modifier.fillMaxSize()
    ) {  innerPadding ->
        if (cameraPermissionState.status.isGranted) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CameraPreview(
                    isTorchEnabled = isTorchEnabled,
                    onQRScanned = {
                        onAction(ScanAction.OnQRScanned(it))
                    }
                )
                CutoutOverlay(
                    cutoutSize = cutoutSize
                )
                Text(
                    text = stringResource(R.string.point_camera),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onOverlay,
                    modifier = Modifier.offset(y = -(cutoutSize / 2) - 40.dp)
                )

                ScanActionButton(
                    enabledIcon = R.drawable.ic_torch_off,
                    disabledIcon = R.drawable.ic_torch,
                    onClick = {
                        isTorchEnabled = !isTorchEnabled
                    },
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                        .align(Alignment.TopStart),
                    enabled = isTorchEnabled
                )

                // The launcher for the photo picker
                val photoPickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickVisualMedia(),
                    onResult = { uri ->
                        uri?.let {
                            onAction(ScanAction.OnPhotoSelected(it))
                        }
                    }
                )

                ScanActionButton(
                    enabledIcon = R.drawable.ic_media,
                    disabledIcon = R.drawable.ic_media,
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                        .align(Alignment.TopEnd),
                )
            }
        }
    }
}