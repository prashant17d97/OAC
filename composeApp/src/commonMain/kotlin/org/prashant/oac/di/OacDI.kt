package org.prashant.oac.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.prashant.oac.MainVM
import org.prashant.oac.data.local.OacDataStore
import org.prashant.oac.data.repository.DataStoreRepositoryImpl
import org.prashant.oac.di.platform.platformModule
import org.prashant.oac.domain.repositories.DataStoreRepository

fun initiateKoin(additionalModules: List<Module>? = null) {
    startKoin {
        modules(additionalModules.orEmpty() + listOf(commonModule, platformModule))
    }
}


val commonModule = module {
    single<OacDataStore> { OacDataStore(dataStore = get()) }
    single<DataStoreRepository> { DataStoreRepositoryImpl(oacDataStore = get()) }
    single<MainVM> { MainVM(dataStoreRepository = get()) }
}