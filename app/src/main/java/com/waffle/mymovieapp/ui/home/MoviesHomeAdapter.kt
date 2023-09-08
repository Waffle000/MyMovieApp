package com.waffle.mymovieapp.ui.home

import android.app.Activity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.thecode.aestheticdialogs.AestheticDialog
import com.thecode.aestheticdialogs.DialogAnimation
import com.thecode.aestheticdialogs.DialogStyle
import com.thecode.aestheticdialogs.DialogType
import com.thecode.aestheticdialogs.OnDialogClickListener
import com.waffle.mymovieapp.R
import com.waffle.mymovieapp.data.response.ResultsItem
import com.waffle.mymovieapp.databinding.ItemDiscoverHomeBinding
import com.waffle.mymovieapp.module.SharedPreferences
import com.waffle.mymovieapp.ui.discover.DiscoverDetailFragment
import com.waffle.mymovieapp.utils.loadImage
import taimoor.sultani.sweetalert2.Sweetalert
import xyz.hasnat.sweettoast.SweetToast

class MoviesHomeAdapter(private val view : Fragment): PagingDataAdapter<ResultsItem, MoviesHomeAdapter.RecyclerViewHolder>(
    NewsComparator
) {
    object NewsComparator : DiffUtil.ItemCallback<ResultsItem>() {
        override fun areItemsTheSame(
            oldItem: ResultsItem,
            newItem: ResultsItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResultsItem,
            newItem: ResultsItem
        ): Boolean {
            return oldItem.equals(newItem)
        }

    }

    inner class RecyclerViewHolder(private val binding: ItemDiscoverHomeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResultsItem) {
            binding.apply {
                tvMovieTitle.text = item.title
                ivThumbnail.loadImage(item.posterPath)
                root.setOnClickListener{
                    if(SharedPreferences(root.context).isLogin) {
                        view.findNavController().navigate(R.id.action_homeFragment_to_discoverDetailFragment, bundleOf(
                            DiscoverDetailFragment.DISCOVER_ID to item.id))
                    } else {
                        Sweetalert.DARK_STYLE = true
                        Sweetalert(root.context, Sweetalert.ERROR_TYPE)
                            .setTitleText("Failed")
                            .setContentText("Please sign in for access movie detail")
                            .showConfirmButton(true)
                            .setConfirmButtonBackgroundColor("#5468ff")
                            .setConfirmButton("Sign In", Sweetalert.OnSweetClickListener { sDialog ->
                                sDialog.dismissWithAnimation()
                                view.findNavController().navigate(R.id.loginFragment)
                            })
                            .setCancelButton(
                                "Cancel"
                            ) { sDialog -> sDialog.dismissWithAnimation() }
                            .show()
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val data = getItem(position)!!
        holder.apply {
            bind(data)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val itemBinding = ItemDiscoverHomeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecyclerViewHolder(itemBinding)
    }
}