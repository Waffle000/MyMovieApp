package com.waffle.mymovieapp.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.waffle.mymovieapp.base.BaseFragment
import com.waffle.mymovieapp.data.response.DiscoverDetailResponse
import com.waffle.mymovieapp.databinding.FragmentTrailerBinding
import com.waffle.mymovieapp.ui.MainViewModel
import org.koin.android.ext.android.inject

class TrailerFragment(private val dataDetail: DiscoverDetailResponse) : BaseFragment() {

    private lateinit var binding : FragmentTrailerBinding

    private val viewModel : MainViewModel by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrailerBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(savedInstanceState: Bundle?) {
        viewModel.getDiscoverTrailer(dataDetail.id ?: 0)
    }

    override fun observeData() {
        viewModel.observeGetDiscoverThrillerSuccess().observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { data ->
                var result = data.results?.filter { it.name == "Official Trailer" || it.name == "Main Trailer" || it.name == "Trailer"}
                binding.apply {
                    viewLifecycleOwner.lifecycle.addObserver(ypTrailer)
                    ypTrailer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            (if(result.isNullOrEmpty().not()) {
                                result?.get(0)?.key ?: ""
                            } else {
                                data.results?.get(0)?.key
                            })?.let { it1 -> youTubePlayer.cueVideo(it1, 0f) } ?: ""
                        }
                    })
                    tvTitleTrailer.text = if(result.isNullOrEmpty().not()) {
                        result?.get(0)?.name
                    } else {
                        data.results?.get(0)?.name
                    }
                }
            }
        }
    }

    override fun init() {

    }
}