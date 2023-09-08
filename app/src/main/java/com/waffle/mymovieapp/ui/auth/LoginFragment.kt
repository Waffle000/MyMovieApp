package com.waffle.mymovieapp.ui.auth

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.waffle.mymovieapp.R
import com.waffle.mymovieapp.base.BaseFragment
import com.waffle.mymovieapp.databinding.FragmentLoginBinding
import com.waffle.mymovieapp.module.SharedPreferences
import com.waffle.mymovieapp.ui.MainViewModel
import com.waffle.mymovieapp.ui.home.HomeFragment
import com.waffle.mymovieapp.utils.disableScreen
import com.waffle.mymovieapp.utils.enableScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import xyz.hasnat.sweettoast.SweetToast

class LoginFragment : BaseFragment() {

    private lateinit var binding : FragmentLoginBinding

    private lateinit var auth: FirebaseAuth

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        auth = Firebase.auth

        activity?.onBackPressedDispatcher?.addCallback(onBackCallback)
        findNavController().addOnDestinationChangedListener { _, destination, _ ->
            onBackCallback.isEnabled = destination.id == R.id.loginFragment
        }
    }

    override fun observeData() {

    }

    override fun init() {
        binding.apply {
            btnSignIn.setOnClickListener{
                if(etEmailLogin.text.toString().isBlank()) {
                    SweetToast.error(requireContext(), "Email is still empty")
                } else if (etPasswordLogin.text.toString().isBlank()) {
                    SweetToast.error(requireContext(), "Password is still empty")
                } else {
                    showLoading()
                    auth.signInWithEmailAndPassword(etEmailLogin.text.toString(), etPasswordLogin.text.toString())
                        .addOnCompleteListener(Activity()) { task ->
                            hideLoading()
                            if (task.isSuccessful) {
                                SweetToast.success(requireContext(), "Sign In Success")
                                val user = auth.currentUser
                                val sharedPreferences = SharedPreferences(requireContext())
                                sharedPreferences.isLogin = true
                                sharedPreferences.userId = user?.uid ?: ""
                                sharedPreferences.userName = user?.email?.substringBefore("@")?.replace(".", " ")?.split(" ")?.joinToString(separator = " ", transform = String::capitalize) ?: ""
                                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                            } else {
                                SweetToast.error(requireContext(), "Authentication failed: ${task.exception}")
                            }
                        }
                }
            }

            tvSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            btnSkipSignIn.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
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
        private const val DELAY_MILIS = 2000L
    }
}