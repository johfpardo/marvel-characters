package com.johfpardo.marvelcharacters.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.johfpardo.marvelcharacters.data.model.CharacterSummary
import com.johfpardo.marvelcharacters.databinding.CharacterListItemBinding
import com.johfpardo.marvelcharacters.ui.adapters.vh.CharactersViewHolder

class CharactersAdapter(
    private val characterItemListener: CharactersViewHolder.CharacterItemListener
) : PagingDataAdapter<CharacterSummary, CharactersViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder =
        CharactersViewHolder(
            CharacterListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            characterItemListener
        )

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<CharacterSummary>() {
            override fun areItemsTheSame(
                oldItem: CharacterSummary,
                newItem: CharacterSummary
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: CharacterSummary,
                newItem: CharacterSummary
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}
