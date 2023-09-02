package com.waffle.mymovieapp.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.waffle.mymovieapp.base.BaseFragment
import com.waffle.mymovieapp.data.response.DiscoverDetailResponse
import com.waffle.mymovieapp.databinding.FragmentReviewBinding
import com.waffle.mymovieapp.ui.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ReviewFragment(private val dataDetail: DiscoverDetailResponse) : BaseFragment() {

    private lateinit var binding: FragmentReviewBinding

    private val viewModel: MainViewModel by inject()

    private lateinit var reviewAdapter: ReviewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        reviewAdapter = ReviewAdapter(this)
    }

    override fun observeData() {
        lifecycleScope.launch {
            viewModel.getDiscoverReview(dataDetail.id ?: 0).collectLatest { data ->
                reviewAdapter.submitData(data)
            }
        }
    }

    override fun init() {
        binding.rvReview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reviewAdapter
        }
    }


}