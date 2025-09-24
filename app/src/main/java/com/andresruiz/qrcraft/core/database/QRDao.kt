package com.andresruiz.qrcraft.core.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface QRDao {
    @Upsert
    suspend fun upsertQR(entity: QREntity): Long

    @Query("DELETE FROM qr WHERE id=:id")
    suspend fun deleteQR(id: Long)

    @Query("SELECT * FROM qr WHERE id=:id")
    fun observeQR(id: Long): Flow<QREntity?>

    @Query("SELECT * FROM qr WHERE isGenerated=1 ORDER BY isFavorite DESC, createdAtMs DESC")
    fun observeGeneratedQRs(): Flow<List<QREntity>>

    @Query("SELECT * FROM qr WHERE isGenerated=0 ORDER BY isFavorite DESC, createdAtMs DESC")
    fun observeScannedQRs(): Flow<List<QREntity>>

}