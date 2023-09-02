package com.waffle.mymovieapp.ui.discover

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.waffle.mymovieapp.R
import com.waffle.mymovieapp.data.response.ResultsItemReview
import com.waffle.mymovieapp.databinding.ItemDiscoverBinding
import com.waffle.mymovieapp.databinding.ItemReviewBinding
import com.waffle.mymovieapp.utils.loadImage

class ReviewAdapter(private val view: Fragment) :
    PagingDataAdapter<ResultsItemReview, ReviewAdapter.RecyclerViewHolder>(NewsComparator) {
    object NewsComparator : DiffUtil.ItemCallback<ResultsItemReview>() {
        override fun areItemsTheSame(
            oldItem: ResultsItemReview,
            newItem: ResultsItemReview
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResultsItemReview,
            newItem: ResultsItemReview
        ): Boolean {
            return oldItem.equals(newItem)
        }

    }

    inner class RecyclerViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResultsItemReview) {
            binding.apply {
                tvNameReview.text = item.author
                tvCommentReview.text = item.content
                tvRatingReview.text = item.authorDetails?.rating
                ciProfile.loadImage(item.authorDetails?.avatarPath, placeholder = R.drawable.ic_user)
                root.setOnClickListener {
                    view.findNavController().navigate(
                        R.id.action_discoverFragment_to_discoverDetailFragment,
                        bundleOf(DiscoverDetailFragment.DISCOVER_ID to item.id)
                    )
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
        val itemBinding = ItemReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecyclerViewHolder(itemBinding)
    }
}