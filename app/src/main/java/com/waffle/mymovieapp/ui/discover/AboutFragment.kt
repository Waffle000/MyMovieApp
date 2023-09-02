package com.waffle.mymovieapp.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.waffle.mymovieapp.base.BaseFragment
import com.waffle.mymovieapp.data.response.DiscoverDetailResponse
import com.waffle.mymovieapp.databinding.FragmentAboutBinding

class AboutFragment(private val dataDetail: DiscoverDetailResponse) : BaseFragment() {

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {

    }

    override fun observeData() {

    }

    override fun init() {
       binding.tvAboutMovie.text = dataDetail.overview
    }

}