package com.andresruiz.qrcraft.core.domain.models

import com.andresruiz.qrcraft.core.domain.QRExt.Companion.getDisplayString
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

data class QR(
    val id: Long,
    val title: String,
    val qrString: String,
    val qrType: QRType,
    val isGenerated: Boolean,
    val isFavorite: Boolean,
    val createdAtMs: Long = System.currentTimeMillis(),
) {
    val displayString: String = this.getDisplayString()
    val displayDate: String = formatUtcMillisToLocalDate(this.createdAtMs)
}


// TODO: Look into a kotlin version of Instant that can covert this
// TODO: also move into a time / date utils class?
/**
 * Converts a UTC timestamp in milliseconds to a formatted date string
 * in the user's local time zone.
 *
 * @param utcMillis The timestamp in UTC milliseconds since the epoch.
 * @return A formatted string e.g., "18 Sep 2025, 10:55".
 */
fun formatUtcMillisToLocalDate(utcMillis: Long): String {
    // 1. Create an Instant from the milliseconds.
    // An Instant represents a single, specific moment in time on the UTC timeline.
    val instant = Instant.ofEpochMilli(utcMillis)

    // 2. Define the desired output format.
    // "MMM" requires a Locale to correctly format the month name (e.g., "Sep").
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm", Locale.getDefault())

    // 3. Apply the device's default time zone to the Instant and format it.
    // This converts the universal time to the user's local wall-clock time.
    return instant.atZone(ZoneId.systemDefault()).format(formatter)
}