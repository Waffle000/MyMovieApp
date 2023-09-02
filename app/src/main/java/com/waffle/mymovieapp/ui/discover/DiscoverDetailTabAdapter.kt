package com.waffle.mymovieapp.ui.discover

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.waffle.mymovieapp.data.response.DiscoverDetailResponse

class DiscoverDetailTabAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val data: DiscoverDetailResponse
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AboutFragment(dataDetail = data)
            1 -> ReviewFragment(dataDetail = data)
            2 -> TrailerFragment(dataDetail = data)
            else -> createFragment(position)
        }
    }
}