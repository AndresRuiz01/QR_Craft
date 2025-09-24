package com.andresruiz.qrcraft.create_details.domain

class QRFormMapper {

    companion object {

        /**
         * Converts a structured [QRForm] object back into its raw string representation for QR code generation.
         *
         * @param qrForm The [QRForm] object to convert.
         * @return The raw string to be encoded in a QR code.
         */
        fun qrFormToString(qrForm: QRForm): String {
            return when (qrForm) {
                is QRForm.Link -> qrForm.url
                is QRForm.Text -> qrForm.text
                is QRForm.PhoneNumber -> "tel:${qrForm.number}"
                is QRForm.Geolocation -> geoToString(qrForm)
                is QRForm.Wifi -> wifiToString(qrForm)
                is QRForm.Contact -> contactToString(qrForm)
            }
        }

        /** Helper function to parse Wi-Fi formatted strings. */
        private fun parseWifi(wifiString: String): QRForm {
            // Extract the content after "WIFI:"
            val content = wifiString.substringAfter(":")
            // Split into key-value pairs and create a map
            val params = content.split(';').filter { it.isNotBlank() }.associate {
                val parts = it.split(':', limit = 2)
                if (parts.size == 2) parts[0] to parts[1] else "" to ""
            }

            val ssid = params["S"] ?: ""
            val psk = params["P"]
            val encryption = params["T"]

            return QRForm.Wifi(ssid, psk ?: "", encryption ?: "")
        }

        /** Helper function to parse Geolocation formatted strings. */
        private fun parseGeo(geoString: String): QRForm {
            val content = geoString.substringAfter("geo:")
            val parts = content.split('?', limit = 2)
            val coordinates = parts[0].split(',', limit = 2)

            return try {
                val lat = coordinates[0].toDouble()
                val lng = coordinates[1].toDouble()
                QRForm.Geolocation(lat.toString(), lng.toString())
            } catch (_ : Exception) {
                // If parsing fails (e.g., malformed numbers), treat as plain text.
                QRForm.Text(geoString)
            }
        }

        /** Helper function to parse vCard formatted strings. */
        private fun parseVCard(vcardString: String): QRForm {
            var name: String? = null
            var phone: String? = null
            var email: String? = null

            vcardString.lines().forEach { line ->
                when {
                    // N:LastName;FirstName
                    line.startsWith("N:", ignoreCase = true) -> {
                        name = line.substringAfter(":")
                    }
                    // TEL;...:<NUMBER>
                    line.contains("TEL", ignoreCase = true) -> {
                        phone = line.substringAfter(":")
                    }
                    // EMAIL;...:<EMAIL>
                    line.contains("EMAIL", ignoreCase = true) -> {
                        email = line.substringAfter(":")
                    }
                }
            }

            // A vCard must at least have a name to be valid.
            return if (name != null) {
                QRForm.Contact(name, email ?: "", phone ?: "")
            } else {
                QRForm.Text(vcardString) // Fallback if no name found.
            }
        }

        /** Helper function to convert a [QRForm.Wifi] object to a string. */
        private fun wifiToString(wifi: QRForm.Wifi): String {
            val ssid = "S:${wifi.ssid};"
            val encryption = wifi.encryption.let { "T:$it;" }
            val psk = wifi.psk.let { "P:$it;" }
            return "WIFI:$ssid$encryption${psk};"
        }

        /** Helper function to convert a [QRForm.Geolocation] object to a string. */
        private fun geoToString(geo: QRForm.Geolocation): String {
            return "geo:${geo.latitude},${geo.longitude}"
        }

        /** Helper function to convert a [QRForm.Contact] object to a vCard string. */
        private fun contactToString(contact: QRForm.Contact): String {
            return buildString {
                appendLine("BEGIN:VCARD")
                appendLine("VERSION:3.0")
                appendLine("N:${contact.name}")
                appendLine("TEL:${contact.phoneNumber}")
                appendLine("EMAIL:${contact.email}")
                append("END:VCARD")
            }
        }

    }
}