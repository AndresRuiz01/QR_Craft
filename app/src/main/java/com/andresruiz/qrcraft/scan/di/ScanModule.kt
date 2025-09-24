package com.andresruiz.qrcraft.scan.di

import com.andresruiz.qrcraft.scan.ScanScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val scanModule = module {
    viewModelOf(::ScanScreenViewModel)
}
