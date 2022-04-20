package com.johfpardo.marvelcharacters.ui.adapters.vh

import androidx.recyclerview.widget.RecyclerView
import com.johfpardo.marvelcharacters.data.model.DetailItem
import com.johfpardo.marvelcharacters.databinding.DetailListItemBinding

class CharacterDetailViewHolder(
    private val binding: DetailListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(detailItem: DetailItem) {
        with(binding) {
            this.detailItem = detailItem
            executePendingBindings()
        }
    }
}
