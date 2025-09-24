package com.andresruiz.qrcraft.core.domain

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.createBitmap
import com.andresruiz.qrcraft.core.domain.models.QR
import com.andresruiz.qrcraft.core.domain.models.QRType
import qrcode.QRCode
import qrcode.internals.QRCodeSquare

class QRExt {

    companion object {

        fun QR.generateQRCodeData(): Array<Array<QRCodeSquare>>? {
            return try {
                val qrCode = QRCode.ofSquares().build(this.qrString)
                qrCode.rawData
            } catch (e: Exception) {
                // Handle exceptions, e.g., content is too long
                e.printStackTrace()
                null
            }
        }

        fun QR.getDisplayString(): String {
            return when (this.qrType) {
                QRType.TEXT -> this.qrString
                QRType.LINK -> sanitizeUrl(this.qrString)
                QRType.CONTACT -> contactDisplayString(this.qrString)
                QRType.PHONENUMBER -> phoneDisplayString(this.qrString)
                QRType.GEOLOCATION -> geoDisplayString(this.qrString)
                QRType.WIFI -> wifiDisplayString(this.qrString)
            }
        }

        fun QR.getQRCodeBitmap(qrData: Array<Array<QRCodeSquare>>): Bitmap? {
            /**
             * An optimized function to convert a 2D ByteArray into a Bitmap.
             * It populates an IntArray with color values and sets them all at once.
             *
             * @param qrData The 2D array from the qrcode-kotlin library.
             * @return A Bitmap object of the QR code.
             */
            val height = qrData.size
            if (height == 0) return null
            val width = qrData[0].size
            if (width == 0) return null

            // Create an array to hold the pixel color data
            val pixels = IntArray(width * height)

            // Populate the pixels array
            for (y in 0 until height) {
                for (x in 0 until width) {
                    val color = if (qrData[y][x].dark) Color.BLACK else Color.WHITE
                    pixels[y * width + x] = color
                }
            }

            // Create a Bitmap and set all the pixels at once
            val bitmap = createBitmap(width, height)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)

            return bitmap
        }

        /**
         * Creates a scaled, high-quality Bitmap from QR code data.
         *
         * @param qrData The 2D array from the qrcode-kotlin library.
         * @param moduleSize The size in pixels for each black/white module of the QR code.
         * A value of 20 is a good starting point for a ~500-600px image.
         * @param quietZoneModules The width of the white border (quiet zone) around the QR code,
         * measured in modules. The standard is 4.
         * @return A high-resolution Bitmap of the QR code.
         */
        fun createScaledQrBitmap(
            qrData: Array<Array<QRCodeSquare>>,
            moduleSize: Int = 20,
            quietZoneModules: Int = 4
        ): Bitmap? {
            val qrHeight = qrData.size
            if (qrHeight == 0) return null
            val qrWidth = qrData[0].size
            if (qrWidth == 0) return null

            // Add padding for the quiet zone
            val paddedWidth = qrWidth + quietZoneModules * 2
            val paddedHeight = qrHeight + quietZoneModules * 2

            // Calculate final bitmap dimensions
            val bitmapWidth = paddedWidth * moduleSize
            val bitmapHeight = paddedHeight * moduleSize

            val bitmap = createBitmap(bitmapWidth, bitmapHeight)

            // Fill the entire bitmap with the quiet zone color (white) first
            bitmap.eraseColor(Color.WHITE)

            // Draw the QR code modules
            for (y in 0 until qrHeight) {
                for (x in 0 until qrWidth) {
                    if (qrData[y][x].dark) {
                        // This module is black, fill the corresponding rect in the bitmap
                        val startX = (x + quietZoneModules) * moduleSize
                        val startY = (y + quietZoneModules) * moduleSize
                        for (pixelY in 0 until moduleSize) {
                            for (pixelX in 0 until moduleSize) {
                                bitmap.setPixel(startX + pixelX, startY + pixelY, Color.BLACK)
                            }
                        }
                    }
                }
            }

            return bitmap
        }

        private fun geoDisplayString(qrString: String): String {
            // parse the qr geo string
            val parts = qrString.substringAfter("geo:").split(",")
            val latitude = parts[0]
            val longitude = parts[1]
            return "$latitude, $longitude"
        }

        private fun wifiDisplayString(qrString: String): String {
            // parse the qr wifi string
            val parts = qrString.substringAfter("WIFI:").split(";")
            val ssid = parts[0].substringAfter("S:")
            val encryption = parts[1].substringAfter("T:")
            val psk = parts[2].substringAfter("P:")
            return "SSID: $ssid\nPassword: $psk\nEncryption type:$encryption"
        }

        private fun contactDisplayString(qrString: String): String {
            // parse the qr contact string
            val parts = qrString.substringAfter("BEGIN:VCARD").split("\n")
            val name = parts.find { it.startsWith("N:") }?.substringAfter("N:") ?: ""
            val email = parts.find { it.startsWith("EMAIL:") }?.substringAfter("EMAIL:") ?: ""
            val phone = parts.find { it.startsWith("TEL:") }?.substringAfter("TEL:") ?: ""
            return "$name\n$email\n$phone"
        }

        private fun phoneDisplayString(qrString: String): String {
            return qrString.substringAfter("tel:")
        }

        private fun sanitizeUrl(url: String): String {
            val regex = "^(https?://)+(?=https?://)".toRegex()
            return url.replaceFirst(regex, "")
        }

    }
}