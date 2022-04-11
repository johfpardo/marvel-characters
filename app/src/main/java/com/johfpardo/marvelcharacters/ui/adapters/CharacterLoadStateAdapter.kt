package com.johfpardo.marvelcharacters.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.johfpardo.marvelcharacters.R
import com.johfpardo.marvelcharacters.ui.adapters.vh.CharacterLoadStateViewHolder

class CharacterLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<CharacterLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: CharacterLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): CharacterLoadStateViewHolder =
        CharacterLoadStateViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.characters_load_state_item, parent, false),
            retry,
        )
}
