package com.andresruiz.qrcraft.root

import androidx.navigation3.runtime.NavKey
import com.andresruiz.qrcraft.core.domain.models.QRType
import kotlinx.serialization.Serializable

@Serializable
data object ScanQR: NavKey
@Serializable
data class ScanResult(val id: Long): NavKey
@Serializable
data object ScanHistory: NavKey
@Serializable
data object CreateQR: NavKey
@Serializable
data class CreateQRDetails(val qrType: QRType): NavKey
@Serializable
data class CreateQRPreview(val id: Long): NavKey