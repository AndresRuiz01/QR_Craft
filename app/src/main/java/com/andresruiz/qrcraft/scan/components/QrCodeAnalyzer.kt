package com.andresruiz.qrcraft.scan.components

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.TimeUnit

class QrCodeAnalyzer(
    private val onQrCodeScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {

    // The cooldown period in milliseconds
    private val debounceInterval = TimeUnit.SECONDS.toMillis(2)
    private var lastScanTime = 0L

    // Get an instance of the ML Kit Barcode Scanner
    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    )

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val currentTime = System.currentTimeMillis()

        // Debounce: if a scan happened recently, skip this frame
        if (currentTime - lastScanTime < debounceInterval) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        barcodes.first().rawValue?.let { qrValue ->
                            // QR Code found!
                            // Update the timestamp to start the cooldown
                            lastScanTime = currentTime
                            // Fire the callback
                            onQrCodeScanned(qrValue)
                        }
                    }
                }
                .addOnCompleteListener {
                    // Always close the imageProxy to allow the next frame to be processed
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
}