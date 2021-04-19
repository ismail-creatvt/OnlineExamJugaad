package com.ismail.creatvt.onlineexamjugaad

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

@BindingAdapter("spanSizeLookup")
fun RecyclerView.setSpanAdapter(lookup: GridLayoutManager.SpanSizeLookup){
    if(layoutManager is GridLayoutManager){
        (layoutManager as GridLayoutManager).spanSizeLookup = lookup
    }
}

@BindingAdapter("loadUrl")
fun ImageView.loadUrl(url: String){
    Glide.with(this)
        .load(url)
        .into(this)
}