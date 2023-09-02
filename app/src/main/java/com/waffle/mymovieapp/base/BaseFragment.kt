package com.waffle.mymovieapp.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment() : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated(savedInstanceState)
        observeData()
        init()
    }

    protected abstract fun onViewCreated(savedInstanceState: Bundle?)

    protected abstract fun observeData()

    protected abstract fun init()
}