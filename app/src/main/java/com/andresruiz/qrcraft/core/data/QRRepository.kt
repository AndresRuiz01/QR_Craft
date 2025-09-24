package com.andresruiz.qrcraft.core.data

import com.andresruiz.qrcraft.core.data.QRMapper.Companion.toEntity
import com.andresruiz.qrcraft.core.data.QRMapper.Companion.toQR
import com.andresruiz.qrcraft.core.database.QRDao
import com.andresruiz.qrcraft.core.domain.models.QR
import com.andresruiz.qrcraft.core.domain.repositories.IQRRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QRRepository(
    val qrDao: QRDao
) : IQRRepository {

    override suspend fun upsertQR(qr: QR): Long {
        return qrDao.upsertQR(qr.toEntity())
    }

    override suspend fun deleteQR(id: Long) {
        qrDao.deleteQR(id)
    }

    override fun observeQR(id: Long): Flow<QR?> {
        return qrDao
            .observeQR(id)
            .map { qrEntity ->
                qrEntity?.toQR()
            }
    }

    override fun observeGeneratedQRs(): Flow<List<QR>> {
        return qrDao
            .observeGeneratedQRs()
            .map { qrEntities ->
                qrEntities.map { qrEntity ->
                    qrEntity.toQR()
                }
            }
    }

    override fun observeScannedQRs(): Flow<List<QR>> {
        return qrDao
            .observeScannedQRs()
            .map { qrEntities ->
                qrEntities.map { qrEntity ->
                    qrEntity.toQR()
                }
            }
    }
}