package com.waffle.mymovieapp.ui.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.waffle.mymovieapp.R
import com.waffle.mymovieapp.base.BaseFragment
import com.waffle.mymovieapp.data.response.GenresItem
import com.waffle.mymovieapp.databinding.FragmentHomeBinding
import com.waffle.mymovieapp.ui.MainViewModel
import com.waffle.mymovieapp.ui.genre.GenreFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


@ExperimentalPagingApi
class HomeFragment : BaseFragment(), GenreHomeAdapter.onItemClick {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: MainViewModel by inject()

    private val onBackCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (doubleBackToExitPressedOnce) {
                requireActivity().finish()
                return
            }

            doubleBackToExitPressedOnce = true
            Toast.makeText(requireContext(), "Click 1 Again For Exit", Toast.LENGTH_SHORT)
                .show()
            lifecycleScope.launch {
                delay(DELAY_MILIS)
                doubleBackToExitPressedOnce = false
            }
        }
    }
    private var doubleBackToExitPressedOnce = false

    private lateinit var discoverAdapter: DiscoverHomeAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        activity?.onBackPressedDispatcher?.addCallback(onBackCallback)
        findNavController().addOnDestinationChangedListener { _, destination, _ ->
            onBackCallback.isEnabled = destination.id == R.id.homeFragment
        }
        val connectivityManager =
            activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        val connected =
            connectivityManager?.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)?.state == NetworkInfo.State.CONNECTED ||
                    connectivityManager?.getNetworkInfo(ConnectivityManager.TYPE_WIFI)?.state == NetworkInfo.State.CONNECTED

        if (connected) {
            viewModel.getGenreList()
            viewModel.getPopularList()
        } else {
            Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_SHORT).show()
        }

        discoverAdapter = DiscoverHomeAdapter(this)
    }

    override fun observeData() {
        viewModel.observeGetGenreSuccess().observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { data ->
                lifecycleScope.launch {
                    viewModel.getDiscoverList(data.genres?.get(0)?.id ?: 0).collectLatest { data ->
                        discoverAdapter.submitData(data)
                    }
                }
                binding.rvGenre.apply {
                    adapter = GenreHomeAdapter(data.genres?.take(8) ?: listOf(), this@HomeFragment)
                    layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                }

            }
        }

        viewModel.observeIsError().observe(viewLifecycleOwner)  {
            it.getContentIfNotHandled()?.let { data ->
                Toast.makeText(requireContext(), data, Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun init() {
        binding.apply {
            rvMoviesByCategory.apply {
                isNestedScrollingEnabled = true
                adapter = discoverAdapter
                layoutManager = GridLayoutManager(requireContext(), 2)
            }

            tvOtherGenre.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_genreFragment)
            }
        }
    }

    override fun setItemClick(data: GenresItem) {
        lifecycleScope.launch {
            viewModel.getDiscoverList(data.id ?: 0).collectLatest { data ->
                discoverAdapter.submitData(data)
            }
        }
    }

    companion object {
        private const val DELAY_MILIS = 2000L
    }

}