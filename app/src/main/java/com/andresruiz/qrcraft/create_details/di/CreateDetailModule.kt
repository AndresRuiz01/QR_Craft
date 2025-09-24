package com.andresruiz.qrcraft.create_details.di

import com.andresruiz.qrcraft.core.domain.models.QRType
import com.andresruiz.qrcraft.create_details.CreateQRDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val createDetailModule = module {
    viewModel { (type: QRType) -> CreateQRDetailViewModel(type, get()) }
}