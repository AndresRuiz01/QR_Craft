package com.andresruiz.qrcraft.history.di

import com.andresruiz.qrcraft.history.HistoryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val historyModule = module {
    viewModelOf(::HistoryViewModel)
}
