package com.andresruiz.qrcraft.preview.di

import com.andresruiz.qrcraft.preview.QRPreviewViewModel
import com.andresruiz.qrcraft.root.ApplicationScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val previewModule = module {
    viewModel { (id: Long) -> QRPreviewViewModel(
        qrRepository = get(),
        application =androidApplication(),
        applicationScope = get(qualifier = ApplicationScope),
        id = id
    ) }
}