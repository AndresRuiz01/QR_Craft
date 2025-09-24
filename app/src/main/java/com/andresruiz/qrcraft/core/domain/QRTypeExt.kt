package com.andresruiz.qrcraft.core.domain

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.andresruiz.qrcraft.R
import com.andresruiz.qrcraft.core.domain.models.QRType
import com.andresruiz.qrcraft.core.presentation.design_system.qrContact
import com.andresruiz.qrcraft.core.presentation.design_system.qrContactBg
import com.andresruiz.qrcraft.core.presentation.design_system.qrGeo
import com.andresruiz.qrcraft.core.presentation.design_system.qrGeoBg
import com.andresruiz.qrcraft.core.presentation.design_system.qrLink
import com.andresruiz.qrcraft.core.presentation.design_system.qrLinkBg
import com.andresruiz.qrcraft.core.presentation.design_system.qrPhone
import com.andresruiz.qrcraft.core.presentation.design_system.qrPhoneBg
import com.andresruiz.qrcraft.core.presentation.design_system.qrText
import com.andresruiz.qrcraft.core.presentation.design_system.qrTextBg
import com.andresruiz.qrcraft.core.presentation.design_system.qrWifi
import com.andresruiz.qrcraft.core.presentation.design_system.qrWifiBg
import com.andresruiz.qrcraft.core.presentation.ui.UiText

class QRTypeExt {
    companion object {
        val QRType.title
            get() = when (this) {
                QRType.TEXT -> UiText.StringResource(R.string.text)
                QRType.LINK -> UiText.StringResource(R.string.link)
                QRType.CONTACT -> UiText.StringResource(R.string.contact)
                QRType.PHONENUMBER -> UiText.StringResource(R.string.phone_number)
                QRType.GEOLOCATION -> UiText.StringResource(R.string.geolocation)
                QRType.WIFI -> UiText.StringResource(R.string.wifi)
            }

        val QRType.icon
            get() = when(this) {
                QRType.TEXT -> R.drawable.ic_text
                QRType.LINK -> R.drawable.ic_link
                QRType.CONTACT -> R.drawable.ic_contact
                QRType.PHONENUMBER -> R.drawable.ic_phone
                QRType.GEOLOCATION -> R.drawable.ic_geo
                QRType.WIFI -> R.drawable.ic_wifi
            }

        val QRType.color
            @Composable
            get() = when (this) {
                QRType.TEXT -> MaterialTheme.colorScheme.qrText
                QRType.LINK -> MaterialTheme.colorScheme.qrLink
                QRType.CONTACT -> MaterialTheme.colorScheme.qrContact
                QRType.PHONENUMBER -> MaterialTheme.colorScheme.qrPhone
                QRType.GEOLOCATION -> MaterialTheme.colorScheme.qrGeo
                QRType.WIFI -> MaterialTheme.colorScheme.qrWifi
            }

        val QRType.bgColor
            @Composable
            get() = when (this) {
                QRType.TEXT -> MaterialTheme.colorScheme.qrTextBg
                QRType.LINK -> MaterialTheme.colorScheme.qrLinkBg
                QRType.CONTACT -> MaterialTheme.colorScheme.qrContactBg
                QRType.PHONENUMBER -> MaterialTheme.colorScheme.qrPhoneBg
                QRType.GEOLOCATION -> MaterialTheme.colorScheme.qrGeoBg
                QRType.WIFI -> MaterialTheme.colorScheme.qrWifiBg
            }


        fun qrStringToQRType(rawValue: String): QRType {
            return when {
                // WiFi: "WIFI:S:<SSID>;T:<WEP|WPA|nopass>;P:<PASSWORD>;H:<true|false>;;"
                rawValue.startsWith("WIFI:", ignoreCase = true) -> QRType.WIFI

                // Geolocation: "geo:<LAT>,<LNG>?q=<QUERY>"
                rawValue.startsWith("geo:", ignoreCase = true) -> QRType.GEOLOCATION

                // Phone Number: "tel:<NUMBER>"
                rawValue.startsWith("tel:", ignoreCase = true) -> QRType.PHONENUMBER

                // Contact Card: "BEGIN:VCARD..."
                rawValue.startsWith("BEGIN:VCARD", ignoreCase = true) -> QRType.CONTACT

                // URL: Check for common URL schemes.
                rawValue.startsWith("http://", ignoreCase = true) ||
                        rawValue.startsWith("https://", ignoreCase = true) ||
                        rawValue.startsWith("www.", ignoreCase = true) -> QRType.LINK

                // Default to plain text if no other format matches.
                else -> QRType.TEXT
            }
        }

    }
}