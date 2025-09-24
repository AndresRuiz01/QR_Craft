package com.andresruiz.qrcraft.core.domain.repositories

import com.andresruiz.qrcraft.core.domain.models.QR
import kotlinx.coroutines.flow.Flow

interface IQRRepository {
    suspend fun upsertQR(qr: QR): Long
    suspend fun deleteQR(id: Long)
    fun observeQR(id: Long): Flow<QR?>
    fun observeGeneratedQRs(): Flow<List<QR>>
    fun observeScannedQRs(): Flow<List<QR>>
}