package com.andresruiz.qrcraft.scan

import android.app.Application
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresruiz.qrcraft.core.domain.QRTypeExt
import com.andresruiz.qrcraft.core.domain.models.QR
import com.andresruiz.qrcraft.core.domain.repositories.IQRRepository
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class ScanScreenViewModel(
    private val qrRepository: IQRRepository,
    private val application: Application
): ViewModel() {

    private val eventChannel = Channel<ScanEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: ScanAction) {
        when(action) {
            is ScanAction.OnQRScanned -> {
                onQRScanned(action.qrString)
            }
            is ScanAction.OnPhotoSelected -> {
                viewModelScope.launch {
                    scanImage(action.uri)
                }
            }
        }
    }

    fun onQRScanned(qrString: String) {
        viewModelScope.launch {
            val id = qrRepository.upsertQR(
                qr = QR(
                    id = 0,
                    title = "",
                    qrString = qrString,
                    isGenerated = false,
                    isFavorite = false,
                    qrType = QRTypeExt.qrStringToQRType(qrString)
                )
            )
            eventChannel.send(ScanEvent.OnQRScanned(id))
        }
    }

    // Refactored scanning logic into a suspending function for cleaner coroutine integration
    suspend fun scanImage(uri: Uri) = suspendCoroutine { continuation ->

        // We're now doing I/O work (decoding a bitmap), so we switch to the IO dispatcher.
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Modern way to decode a bitmap from a URI
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(application.contentResolver, uri))
        } else {
            // Deprecated but necessary for older APIs
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(application.contentResolver, uri)
        }

        // Create the InputImage from our high-resolution bitmap
        val image = InputImage.fromBitmap(bitmap, 0) // 0 rotation

        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
        val scanner = BarcodeScanning.getClient(options)

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                val result = barcodes.firstOrNull()?.rawValue
                if (result != null) {
                    onQRScanned(result)
                } else {
                    // display error view
                }
                continuation.resume(Unit)
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }
}