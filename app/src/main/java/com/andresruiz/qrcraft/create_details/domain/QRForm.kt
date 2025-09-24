package com.andresruiz.qrcraft.create_details.domain

import android.util.Patterns

sealed class QRForm {
    // For QR codes that just contain a URL
    data class Link(val url: String) : QRForm()

    // For plain text QR codes
    data class Text(val text: String) : QRForm()

    // For contact information (vCard format)
    data class Contact(
        val name: String,
        val email: String,
        val phoneNumber: String,
    ) : QRForm()

    // For geographic coordinates
    data class Geolocation(
        val latitude: String,
        val longitude: String,
    ) : QRForm()

    // For initiating a phone call
    data class PhoneNumber(val number: String) : QRForm()

    // For connecting to a Wi-Fi network
    data class Wifi(
        val ssid: String,
        val psk: String, // Pre-Shared Key (password)
        val encryption: String // e.g., "WPA", "WEP"
    ) : QRForm()
}

fun QRForm.isValid(): Boolean {
    when(this) {
        is QRForm.Contact -> {
            return this.name.isNotEmpty() &&
                    Patterns.EMAIL_ADDRESS.matcher(this.email).matches() &&
                    Patterns.PHONE.matcher(this.phoneNumber).matches()
        }
        is QRForm.Geolocation -> {
            try {
                this.latitude.toDouble()
                this.longitude.toDouble()
                return true
            } catch (_: Exception) {
                return false
            }
        }
        is QRForm.Link -> {
            return Patterns.WEB_URL.matcher(this.url).matches()
        }
        is QRForm.PhoneNumber -> {
            return Patterns.PHONE.matcher(this.number).matches()
        }
        is QRForm.Text -> {
            return this.text.isNotEmpty()
        }
        is QRForm.Wifi -> {
            return this.ssid.isNotEmpty() && this.psk.isNotEmpty() && this.encryption.isNotEmpty()
        }
    }
}