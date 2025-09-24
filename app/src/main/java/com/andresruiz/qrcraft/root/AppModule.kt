package com.andresruiz.qrcraft.root

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module

val ApplicationScope = named("ApplicationScope")

val appModule = module {
    // Defines a singleton instance of CoroutineScope
    single(qualifier = ApplicationScope) {
        // SupervisorJob makes sure that if one coroutine fails, the whole scope is not cancelled.
        // Dispatchers.Default is for CPU-intensive work.
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

}