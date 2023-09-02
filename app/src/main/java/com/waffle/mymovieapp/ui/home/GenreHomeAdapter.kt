package com.waffle.mymovieapp.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.waffle.mymovieapp.R
import com.waffle.mymovieapp.data.response.GenresItem
import com.waffle.mymovieapp.databinding.ItemGenreBinding
import com.waffle.mymovieapp.databinding.ItemGenreHomeBinding
import com.waffle.mymovieapp.ui.discover.DiscoverFragment

class GenreHomeAdapter(
    private var genreList : List<GenresItem>, private var clickListener : onItemClick
) : RecyclerView.Adapter<GenreHomeAdapter.ViewHolder>(){
    //    private var backupPromoList = promoList.toList()
    class ViewHolder(val binding : ItemGenreHomeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_genre_home, parent, false))
    }

    var selectedPosition = 0
    var lastSelectedPosition = -1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val genre = genreList[position]
        holder.itemView.setOnClickListener {

            lastSelectedPosition = selectedPosition
            selectedPosition = holder.bindingAdapterPosition
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
            clickListener.setItemClick(genre)
        }
        if (selectedPosition == holder.bindingAdapterPosition) {
            holder.binding.llParent.setBackgroundResource(R.drawable.bg_rounded_grey_full_stroke)
        } else {
            holder.binding.llParent.setBackgroundResource(R.drawable.bg_rounded_grey_full)
        }
        bindDataToView(binding, genre)
    }

    override fun getItemCount() = genreList.size

    private fun bindDataToView(binding: ItemGenreHomeBinding, genre :GenresItem){
        binding.apply {
            tvNameGenre.text = genre.name
        }
    }

    interface onItemClick {
        fun setItemClick(data: GenresItem)
    }
}