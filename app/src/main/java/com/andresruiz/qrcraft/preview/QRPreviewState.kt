package com.andresruiz.qrcraft.preview

import com.andresruiz.qrcraft.core.domain.models.QR

data class QRPreviewState(
    val qr: QR,
    val title: String,
)