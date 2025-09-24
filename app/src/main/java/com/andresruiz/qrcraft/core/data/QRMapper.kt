package com.andresruiz.qrcraft.core.data

import com.andresruiz.qrcraft.core.database.QREntity
import com.andresruiz.qrcraft.core.domain.QRTypeExt
import com.andresruiz.qrcraft.core.domain.models.QR

class QRMapper {
    companion object {
        fun QR.toEntity(): QREntity {
            return QREntity(
                id = id,
                title = title,
                qrString = qrString,
                isGenerated = isGenerated,
                isFavorite = isFavorite,
                createdAtMs = createdAtMs
            )
        }

        fun QREntity.toQR(): QR {
            return QR(
                id = id,
                title = title,
                qrString = qrString,
                isGenerated = isGenerated,
                createdAtMs = createdAtMs,
                isFavorite = isFavorite,
                qrType = QRTypeExt.qrStringToQRType(qrString)
            )
        }
    }
}

