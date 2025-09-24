package com.andresruiz.qrcraft.scan

import android.net.Uri

sealed interface ScanAction {
    data class OnQRScanned(val qrString: String): ScanAction
    data class OnPhotoSelected(val uri: Uri): ScanAction
}