package com.johfpardo.marvelcharacters.ui.adapters.vh

import androidx.recyclerview.widget.RecyclerView
import com.johfpardo.marvelcharacters.data.model.CharacterSummary
import com.johfpardo.marvelcharacters.databinding.CharacterListItemBinding

class CharactersViewHolder(
    private val binding: CharacterListItemBinding,
    private val characterItemListener: CharacterItemListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(character: CharacterSummary) {
        with(binding) {
            this.character = character
            this.setClickListener {
                characterItemListener.onItemClicked(character.id)
            }
            executePendingBindings()
        }
    }

    interface CharacterItemListener {
        fun onItemClicked(characterId: Int?)
    }
}
