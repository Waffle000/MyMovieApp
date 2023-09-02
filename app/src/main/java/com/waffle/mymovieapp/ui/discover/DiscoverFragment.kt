package com.waffle.mymovieapp.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.waffle.mymovieapp.base.BaseFragment
import com.waffle.mymovieapp.databinding.FragmentDiscoverBinding
import com.waffle.mymovieapp.ui.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class DiscoverFragment : BaseFragment() {

    private lateinit var binding : FragmentDiscoverBinding

    private val viewModel : MainViewModel by inject()

    private lateinit var discoverAdapter: DiscoverAdapter

    private val genreId by lazy {
        arguments?.getInt(GENRE_ID) ?: 0
    }

    private val genreName by lazy {
        arguments?.getString(GENRE_NAME) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiscoverBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        discoverAdapter = DiscoverAdapter(this)
    }

    override fun observeData() {
        lifecycleScope.launch {
            viewModel.getDiscoverList(genreId).collectLatest { data ->
                discoverAdapter.submitData(data)
            }
        }
    }

    override fun init() {
        binding.apply {
            rvHome.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = discoverAdapter
            }
            tvTitleMovie.text = genreName
            btnBack.setOnClickListener{
                findNavController().popBackStack()
            }
        }
    }

    companion object {
        const val GENRE_ID = "DiscoverFragment.genreId"
        const val GENRE_NAME = "DiscoverFragment.genreName"
    }


}