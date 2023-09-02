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
import com.waffle.mymovieapp.data.response.ResultsItem
import com.waffle.mymovieapp.databinding.ItemDiscoverBinding
import com.waffle.mymovieapp.utils.loadImage

class DiscoverAdapter(private val view : Fragment): PagingDataAdapter<ResultsItem, DiscoverAdapter.RecyclerViewHolder>(NewsComparator) {
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

    inner class RecyclerViewHolder(private val binding: ItemDiscoverBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResultsItem) {
            binding.apply {
                tvMovieTitle.text = item.title
                tvMovieRating.text = item.voteAverage
                tvMovieYear.text = item.releaseDate
                tvMoviePopularity.text = item.popularity
                ivThumbnail.loadImage(item.posterPath)
                root.setOnClickListener{
                    view.findNavController().navigate(R.id.action_discoverFragment_to_discoverDetailFragment, bundleOf(DiscoverDetailFragment.DISCOVER_ID to item.id))
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
        val itemBinding = ItemDiscoverBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecyclerViewHolder(itemBinding)
    }
}