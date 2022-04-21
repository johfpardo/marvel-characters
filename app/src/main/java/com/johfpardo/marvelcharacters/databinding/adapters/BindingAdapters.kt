package com.johfpardo.marvelcharacters.databinding.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.johfpardo.marvelcharacters.R
import com.johfpardo.marvelcharacters.data.model.DetailItem
import com.squareup.picasso.Picasso

@BindingAdapter("isVisible")
fun bindIsVisible(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Picasso.get().load(imageUrl).into(view)
    }
}

@BindingAdapter("detailItemStyle")
fun bindDetailItemStyle(view: TextView, detailItem: DetailItem) {
    when (detailItem) {
        is DetailItem.Title -> view.setTextAppearance(R.style.DetailTitle)
        is DetailItem.Item -> view.setTextAppearance(R.style.DetailItem)
    }
}
