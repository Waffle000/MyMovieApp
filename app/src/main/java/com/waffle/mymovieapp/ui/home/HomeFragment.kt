package com.waffle.mymovieapp.ui.home

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.waffle.mymovieapp.R
import com.waffle.mymovieapp.base.BaseFragment
import com.waffle.mymovieapp.data.response.GenresItem
import com.waffle.mymovieapp.databinding.FragmentHomeBinding
import com.waffle.mymovieapp.module.SharedPreferences
import com.waffle.mymovieapp.service.Constants
import com.waffle.mymovieapp.service.NotificationService
import com.waffle.mymovieapp.ui.MainViewModel
import com.waffle.mymovieapp.utils.disableScreen
import com.waffle.mymovieapp.utils.enableScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import taimoor.sultani.sweetalert2.Sweetalert
import xyz.hasnat.sweettoast.SweetToast


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

    private lateinit var discoverAdapter: MoviesHomeAdapter

    private lateinit var popularAdapter: MoviesHomeAdapter

    private lateinit var nowPlayingAdapter: MoviesHomeAdapter

    private lateinit var topRatedAdapter: MoviesHomeAdapter

    private lateinit var upcomingAdapter: MoviesHomeAdapter
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
            showLoading()
            viewModel.getGenreList()
            viewModel.getPopularImage()
        } else {
            SweetToast.error(requireContext(), "No Internet Connection")
        }

        discoverAdapter = MoviesHomeAdapter(this)

        popularAdapter = MoviesHomeAdapter(this)

        nowPlayingAdapter = MoviesHomeAdapter(this)

        topRatedAdapter = MoviesHomeAdapter(this)

        upcomingAdapter = MoviesHomeAdapter(this)
    }

    override fun observeData() {
        viewModel.apply {
            observeGetGenreSuccess().observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let { data ->
                    lifecycleScope.launch {
                        getDiscoverList(data.genres?.get(0)?.id ?: 0).collectLatest { data ->
                            discoverAdapter.submitData(data)
                        }
                    }
                    binding.rvGenre.apply {
                        adapter =
                            GenreHomeAdapter(data.genres?.take(8) ?: listOf(), this@HomeFragment)
                        layoutManager =
                            LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                    }
                    hideLoading()
                }
            }

            observeIsError().observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let { data ->
                    Toast.makeText(requireContext(), data, Toast.LENGTH_SHORT).show()
                }
            }

            observeGetPopularListSuccess().observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let { data ->
                    val adapter = SliderAdapter(data.results?.take(3) ?: listOf())
                    binding.imageSlider.apply {
                        setSliderAdapter(adapter)
                        setIndicatorAnimation(IndicatorAnimationType.WORM)
                        setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
                        setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH)
                        setIndicatorSelectedColor(Color.WHITE)
                        setIndicatorUnselectedColor(Color.GRAY)
                        setScrollTimeInSec(4)
                        startAutoCycle()
                    }
                    hideLoading()
                }
            }

            lifecycleScope.launch {
                getPopularList().collectLatest { data ->
                    popularAdapter.submitData(data)
                }
            }

            lifecycleScope.launch {
                getNowPlayingList().collectLatest { data ->
                    nowPlayingAdapter.submitData(data)
                }
            }

            lifecycleScope.launch {
                getTopRatedList().collectLatest { data ->
                    topRatedAdapter.submitData(data)
                }
            }

            lifecycleScope.launch {
                getUpcomingList().collectLatest { data ->
                    upcomingAdapter.submitData(data)
                }
            }

        }
    }

    override fun init() {
        binding.apply {
            if (SharedPreferences(requireContext()).userName != "") {
                tvTitleGenre.text =
                    "Hello ${SharedPreferences(requireContext()).userName}, \nWhat Movies Today?"
                btnLogout.isGone = false
                btnNotifOn.isGone = false
            }

            btnLogout.setOnClickListener {
                Sweetalert.DARK_STYLE = true
                Sweetalert(requireContext(), Sweetalert.WARNING_TYPE)
                    .setTitleText("Logout")
                    .setContentText("Are you sure you want to logout?")
                    .showConfirmButton(true)
                    .setConfirmButtonBackgroundColor("#5468ff")
                    .setConfirmButton("Logout", Sweetalert.OnSweetClickListener { sDialog ->
                        sDialog.dismissWithAnimation()
                        SharedPreferences(requireContext()).resetSharedPref()
                        Firebase.auth.signOut()
                        findNavController().navigate(R.id.loginFragment)
                    })
                    .setCancelButton(
                        "Cancel"
                    ) { sDialog -> sDialog.dismissWithAnimation() }
                    .show()
            }


            btnNotifOn.setOnClickListener {
                startNotificationService()
                btnNotifOff.isGone = false
                btnNotifOn.isGone = true
            }

            btnNotifOff.setOnClickListener {
                stopNotificationService()
                btnNotifOff.isGone = true
                btnNotifOn.isGone = false
            }

            rvMoviesByCategory.apply {
                adapter = discoverAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }

            rvMoviesPopular.apply {
                adapter = popularAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }

            rvMoviesNowPlaying.apply {
                adapter = nowPlayingAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }

            rvMoviesTopRated.apply {
                adapter = topRatedAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }

            rvMoviesUpcoming.apply {
                adapter = upcomingAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }

            tvOtherGenre.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_genreFragment)
            }
        }
    }

    private fun startNotificationService() {
        val intent = Intent(requireContext(), NotificationService::class.java)
        intent.action = Constants.ACTION_START_NOTIFICATION_SERVICE
        activity?.startService(intent)
        SweetToast.success(requireContext(), "Notification service started")

    }

    private fun stopNotificationService() {
        val intent = Intent(activity?.applicationContext, NotificationService::class.java)
        intent.action = Constants.ACTION_STOP_NOTIFICATION_SERVICE
        activity?.startService(intent)
        SweetToast.error(requireContext(), "Notification service stopped")
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

    private fun showLoading(){
        activity?.disableScreen()
        binding.laLoading.visibility = View.VISIBLE
    }

    private fun hideLoading(){
        activity?.enableScreen()
        binding.laLoading.visibility = View.GONE
    }

}