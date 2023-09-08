package com.waffle.mymovieapp.ui.auth

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.waffle.mymovieapp.R
import com.waffle.mymovieapp.base.BaseFragment
import com.waffle.mymovieapp.databinding.FragmentRegisterBinding
import com.waffle.mymovieapp.utils.disableScreen
import com.waffle.mymovieapp.utils.enableScreen
import xyz.hasnat.sweettoast.SweetToast

class RegisterFragment : BaseFragment() {

    private lateinit var binding : FragmentRegisterBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        auth = Firebase.auth
    }

    override fun observeData() {

    }

    override fun init() {
        binding.apply {
            btnSignUp.setOnClickListener {
                when {
                    etEmailRegister.text.toString().isBlank() -> {
                        SweetToast.error(requireContext(), "Email is still empty")
                    }
                    etPasswordRegister.text.toString().isBlank() -> {
                        SweetToast.error(requireContext(), "Password is still empty")
                    }
                    etConfirmPasswordRegister.toString().isBlank() -> {
                        SweetToast.error(requireContext(), "Confirm Password is still empty")
                    }
                    etPasswordRegister.text.toString() != etConfirmPasswordRegister.text.toString() -> {
                        SweetToast.error(requireContext(), "Password is not same")
                    }
                    else -> {
                        showLoading()
                        auth.createUserWithEmailAndPassword(etEmailRegister.text.toString(), etPasswordRegister.text.toString())
                            .addOnCompleteListener(Activity()) { task ->
                                hideLoading()
                                if (task.isSuccessful) {
                                    SweetToast.success(requireContext(), "Sign Up Success")
                                    findNavController().popBackStack()
                                } else {
                                    SweetToast.error(requireContext(), "Authentication failed: ${task.exception}")
                                }
                            }
                    }
                }
            }

            tvSignIn.setOnClickListener {
                findNavController().popBackStack()
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
}