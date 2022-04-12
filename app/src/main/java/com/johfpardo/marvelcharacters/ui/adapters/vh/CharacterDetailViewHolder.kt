package com.johfpardo.marvelcharacters.ui.adapters.vh

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.johfpardo.marvelcharacters.R
import com.johfpardo.marvelcharacters.data.model.DetailItem

class CharacterDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvText: TextView = itemView.findViewById(R.id.tv_text)

    fun bind(detailItem: DetailItem) {
        tvText.text = detailItem.text
        when (detailItem) {
            is DetailItem.Title -> tvText.setTextAppearance(R.style.DetailTitle)

            is DetailItem.Item -> tvText.setTextAppearance(R.style.DetailItem)
        }
    }
}
