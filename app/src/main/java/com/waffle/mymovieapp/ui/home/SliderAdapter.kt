package com.waffle.mymovieapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.smarteist.autoimageslider.SliderViewAdapter
import com.waffle.mymovieapp.data.response.ResultsItemPopular
import com.waffle.mymovieapp.databinding.ItemImageSliderBinding
import com.waffle.mymovieapp.utils.loadImage


class SliderAdapter(private val mSliderItems : List<ResultsItemPopular>) :
    SliderViewAdapter<SliderAdapter.SliderAdapterVH>() {

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val itemBinding = ItemImageSliderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SliderAdapterVH(itemBinding)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        val sliderItem = mSliderItems[position]
        viewHolder.apply {
            bind(sliderItem)
        }
    }

    override fun getCount(): Int {
        //slider view count could be dynamic size
        return mSliderItems.size
    }

    inner class SliderAdapterVH(private val binding: ItemImageSliderBinding) :
        SliderViewAdapter.ViewHolder(binding.root) {
        fun bind(item: ResultsItemPopular) {
            binding.apply {
                ivAutoImageSlider.loadImage(item.backdropPath)
            }
        }
    }
}