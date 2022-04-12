package com.johfpardo.marvelcharacters.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.johfpardo.marvelcharacters.R
import com.johfpardo.marvelcharacters.data.model.DetailItem
import com.johfpardo.marvelcharacters.ui.adapters.vh.CharacterDetailViewHolder

class CharacterDetailAdapter(
    private val list: ArrayList<DetailItem>
) : RecyclerView.Adapter<CharacterDetailViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterDetailViewHolder =
        CharacterDetailViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.detail_list_item, parent, false)
        )

    override fun onBindViewHolder(holder: CharacterDetailViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addItems(items: List<DetailItem>) {
        list.addAll(items)
        notifyDataSetChanged()
    }
}
