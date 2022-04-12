package com.johfpardo.marvelcharacters.ui.adapters.vh

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.johfpardo.marvelcharacters.R
import com.johfpardo.marvelcharacters.data.model.Character
import com.squareup.picasso.Picasso

class CharactersViewHolder(
    itemView: View,
    private val characterItemListener: CharacterItemListener
) : RecyclerView.ViewHolder(itemView) {
    private val ivAvatar: ImageView = itemView.findViewById(R.id.iv_avatar)
    private val tvName: TextView = itemView.findViewById(R.id.tv_name)
    private val tvDescription: TextView = itemView.findViewById(R.id.tv_description)

    fun bind(character: Character) {
        tvName.text = character.name
        tvDescription.text = character.description
        Picasso.get().load(character.thumbnail?.fullPath).into(ivAvatar)
        itemView.setOnClickListener {
            characterItemListener.onItemClicked(character.id)
        }
    }

    interface CharacterItemListener {
        fun onItemClicked(characterId: Int?)
    }
}
