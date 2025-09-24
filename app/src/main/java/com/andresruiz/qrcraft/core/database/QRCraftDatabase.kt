package com.andresruiz.qrcraft.core.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [
        QREntity::class
    ],
    version = 1
)
abstract class QRCraftDatabase : RoomDatabase() {
     abstract val qrDao: QRDao

}