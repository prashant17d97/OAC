package org.prashant.oac.di.platform

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.prashant.oac.data.local.dataStore

actual val platformModule: Module
    get() = module {
        single(named("Platform")) { "Android" }
        // Provides a DataStore instance for managing preferences on the iOS side
        single<DataStore<Preferences>> { dataStore(context = get()) }

    }