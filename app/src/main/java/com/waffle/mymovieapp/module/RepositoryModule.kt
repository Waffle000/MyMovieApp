package com.waffle.mymovieapp.module

import com.waffle.mymovieapp.data.AppRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        AppRepository(get())
    }
}