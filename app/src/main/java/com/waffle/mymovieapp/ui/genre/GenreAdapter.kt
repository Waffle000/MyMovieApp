package com.waffle.mymovieapp.ui.genre

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.waffle.mymovieapp.R
import com.waffle.mymovieapp.data.response.DiscoverResponse
import com.waffle.mymovieapp.data.response.GenresItem
import com.waffle.mymovieapp.databinding.ItemGenreBinding
import com.waffle.mymovieapp.ui.discover.DiscoverFragment

class GenreAdapter(
    private var genreList : List<GenresItem>, private val view: Fragment
) : RecyclerView.Adapter<GenreAdapter.ViewHolder>(){
    //    private var backupPromoList = promoList.toList()
    class ViewHolder(val binding : ItemGenreBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_genre, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val genre = genreList[position]
        holder.itemView.setOnClickListener {
            view.findNavController().navigate(R.id.action_genreFragment_to_discoverFragment, bundleOf(
                DiscoverFragment.GENRE_ID to genre.id, DiscoverFragment.GENRE_NAME to genre.name))
        }
        bindDataToView(binding, genre)
    }

    override fun getItemCount() = genreList.size

    private fun bindDataToView(binding: ItemGenreBinding, genre :GenresItem){
        binding.apply {
            tvNameGenre.text = genre.name
        }
    }
}