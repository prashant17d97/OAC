package org.prashant.oac.di

import android.app.Application
import android.content.Context
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module

class Oac : Application() {
    override fun onCreate() {
        super.onCreate()
        initiateKoin(
            listOf(
                module {
                    single<Context> { this@Oac }
                }
            )
        )

    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}