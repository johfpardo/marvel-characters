package com.johfpardo.marvelcharacters.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.johfpardo.marvelcharacters.R
import com.johfpardo.marvelcharacters.data.model.Character
import com.squareup.picasso.Picasso

class CharactersAdapter(private val characters: ArrayList<Character>):
    RecyclerView.Adapter<CharactersAdapter.CharactersViewHolder>() {

    inner class CharactersViewHolder internal constructor(itemView: View):
        RecyclerView.ViewHolder(itemView) {
        val ivAvatar: ImageView = itemView.findViewById(R.id.iv_avatar)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_description)

        fun bind(character: Character) {
            tvName.text = character.name
            tvDescription.text = character.description
            Picasso.get().load(character.thumbnail?.fullPath).into(ivAvatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder =
        CharactersViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.character_list_item, parent, false))

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        holder.bind(characters[position])
    }

    override fun getItemCount(): Int = characters.size

    fun addCharacters(newCharacters: List<Character>) {
        characters.addAll(newCharacters)
        notifyDataSetChanged()
    }
}
