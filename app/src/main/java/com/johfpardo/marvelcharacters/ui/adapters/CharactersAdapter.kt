package com.johfpardo.marvelcharacters.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.johfpardo.marvelcharacters.R
import com.johfpardo.marvelcharacters.data.model.Character
import com.johfpardo.marvelcharacters.ui.adapters.vh.CharactersViewHolder

class CharactersAdapter : PagingDataAdapter<Character, CharactersViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder =
        CharactersViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.character_list_item, parent, false)
        )

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}
