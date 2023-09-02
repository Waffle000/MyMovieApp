package com.waffle.mymovieapp

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import com.waffle.mymovieapp.module.networkModule
import com.waffle.mymovieapp.module.repositoryModule
import com.waffle.mymovieapp.module.viewModelModule
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@ExperimentalPagingApi
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@App)
            modules(listOf(networkModule, repositoryModule, viewModelModule))
        }
    }
}