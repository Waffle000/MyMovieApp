package com.waffle.mymovieapp.ui.genre

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.waffle.mymovieapp.R
import com.waffle.mymovieapp.base.BaseFragment
import com.waffle.mymovieapp.databinding.FragmentGenreBinding
import com.waffle.mymovieapp.ui.MainViewModel
import com.waffle.mymovieapp.utils.disableScreen
import com.waffle.mymovieapp.utils.enableScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class GenreFragment : BaseFragment() {


    private lateinit var binding : FragmentGenreBinding

    private val viewModel : MainViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGenreBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        viewModel.getGenreList()
        showLoading()
    }

    override fun observeData() {
        viewModel.apply {
            observeGetGenreSuccess().observe(viewLifecycleOwner) {
                it.getContentIfNotHandled().let {data ->
                    binding.rvGenre.apply {
                        adapter = GenreAdapter(data?.genres ?: listOf(), this@GenreFragment)
                        layoutManager = GridLayoutManager(requireContext(), 2)
                    }
                    hideLoading()
                }
            }
        }
    }

    override fun init() {
    }

    private fun showLoading(){
        activity?.disableScreen()
        binding.laLoading.visibility = View.VISIBLE
    }

    private fun hideLoading(){
        activity?.enableScreen()
        binding.laLoading.visibility = View.GONE
    }

}