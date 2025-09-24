package com.andresruiz.qrcraft

import android.app.Application
import com.andresruiz.qrcraft.core.di.coreModule
import com.andresruiz.qrcraft.create_details.di.createDetailModule
import com.andresruiz.qrcraft.history.di.historyModule
import com.andresruiz.qrcraft.preview.di.previewModule
import com.andresruiz.qrcraft.root.appModule
import com.andresruiz.qrcraft.scan.di.scanModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class QRCraftApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@QRCraftApplication)
            modules(
                appModule,
                createDetailModule,
                coreModule,
                historyModule,
                scanModule,
                previewModule
            )
        }
    }
}