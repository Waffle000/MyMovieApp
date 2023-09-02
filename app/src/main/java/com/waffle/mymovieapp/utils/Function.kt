package com.waffle.mymovieapp.utils

import android.app.Activity
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.waffle.mymovieapp.R

fun ImageView.loadImage(url: String?, placeholder: Int = R.drawable.ic_launcher_foreground) {
    Glide.with(this)
        .load("https://image.tmdb.org/t/p/original/$url")
        .apply(
            RequestOptions()
                .centerCrop()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(placeholder)
                .error(placeholder)
        )
        .into(this)
}

internal fun Activity.disableScreen(){
    window.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
}

internal fun Activity.enableScreen(){
    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
}