package com.andresruiz.qrcraft.core.di

import androidx.room.Room
import com.andresruiz.qrcraft.core.data.QRRepository
import com.andresruiz.qrcraft.core.database.QRCraftDatabase
import com.andresruiz.qrcraft.core.domain.repositories.IQRRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreModule = module {
    single {
        val dbName = "qrcraft.db"
        val builder = Room.databaseBuilder(
            androidApplication(),
            QRCraftDatabase::class.java,
            dbName
        )
        builder.build()
    }
    single { get<QRCraftDatabase>().qrDao }
    singleOf(::QRRepository) { bind<IQRRepository>() }
}