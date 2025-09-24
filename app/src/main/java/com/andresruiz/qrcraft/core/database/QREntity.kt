package com.andresruiz.qrcraft.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "qr",)
data class QREntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val title: String,
    val qrString: String,
    val isGenerated: Boolean,
    val createdAtMs: Long,
    val isFavorite: Boolean
)