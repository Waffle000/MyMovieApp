package com.waffle.mymovieapp.ui.discover

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.waffle.mymovieapp.R
import com.waffle.mymovieapp.base.BaseFragment
import com.waffle.mymovieapp.data.response.DiscoverDetailResponse
import com.waffle.mymovieapp.databinding.FragmentDiscoverDetailBinding
import com.waffle.mymovieapp.ui.MainViewModel
import com.waffle.mymovieapp.utils.disableScreen
import com.waffle.mymovieapp.utils.enableScreen
import com.waffle.mymovieapp.utils.loadImage
import org.koin.android.ext.android.inject

class DiscoverDetailFragment() : BaseFragment() {

    private lateinit var binding: FragmentDiscoverDetailBinding

    private val viewModel: MainViewModel by inject()

    private val discoverId by lazy {
        arguments?.getInt(DISCOVER_ID) ?: 0
    }

    private val onBackCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiscoverDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        showLoading()
        viewModel.getDiscoverDetail(discoverId)
    }

    override fun onResume() {
        super.onResume()
        activity?.onBackPressedDispatcher?.addCallback(onBackCallback)
        findNavController().addOnDestinationChangedListener { _, destination, _ ->
            onBackCallback.isEnabled = destination.id == R.id.discoverDetailFragment
        }
    }

    override fun observeData() {
        viewModel.apply {
            observeGetDiscoverDetailSuccess().observe(viewLifecycleOwner) {
                it.getContentIfNotHandled().let { data ->
                    initTabViewPager(data ?: DiscoverDetailResponse())
                    var genreList: List<String> = listOf()
                    data?.genres?.map {
                        genreList += it?.name ?: ""
                    }
                    binding.apply {
                        tvMovieTitle.text = data?.title
                        tvMovieYearAndDuration.text =
                            "${data?.releaseDate} | ${data?.runtime} Minute"
                        tvMovieGenre.text = genreList.joinToString(" | ")
                        tvMovieRating.text = data?.voteAverage
                        ivBackdrop.loadImage(data?.backdropPath)
                        ivThumbnail.loadImage(data?.posterPath)
                    }
                    hideLoading()
                }
            }
            observeIsError().observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let { result ->
                    Log.e("TAG", "observeData: $result")
                    Toast.makeText(requireContext(), "$result", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun init() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initTabViewPager(data: DiscoverDetailResponse) {
        val tabAdapter = DiscoverDetailTabAdapter(childFragmentManager, lifecycle, data)
        with(binding) {
            vpDiscoverDetail.adapter = tabAdapter
            TabLayoutMediator(tlDiscoverDetail, vpDiscoverDetail) { tabText, position ->
                tabText.text = when (position) {
                    0 -> getString(R.string.about_movie)
                    1 -> getString(R.string.review)
                    2 -> getString(R.string.trailer)
                    else -> getString(R.string.about_movie)
                }
            }.attach()
        }
    }

    private fun showLoading(){
        activity?.disableScreen()
        binding.laLoading.visibility = View.VISIBLE
    }

    private fun hideLoading(){
        activity?.enableScreen()
        binding.laLoading.visibility = View.GONE
    }
    companion object {
        const val DISCOVER_ID = "DiscoverDetailFragment.discoverid"
    }
}