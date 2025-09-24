package com.andresruiz.qrcraft.scan

import android.content.Context
import android.net.Uri

interface ScanEvent {
    data class OnQRScanned(val id: Long): ScanEvent
    data class OnPhotoSelected(val uri: Uri, val context: Context): ScanEvent
}