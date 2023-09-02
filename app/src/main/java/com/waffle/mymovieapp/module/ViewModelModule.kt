package com.waffle.mymovieapp.module

import com.waffle.mymovieapp.ui.MainViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { MainViewModel(get()) }
}