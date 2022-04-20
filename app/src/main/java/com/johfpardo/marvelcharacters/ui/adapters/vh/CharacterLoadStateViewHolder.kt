package com.johfpardo.marvelcharacters.ui.adapters.vh

import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.johfpardo.marvelcharacters.databinding.CharactersLoadStateItemBinding
import com.johfpardo.marvelcharacters.ui.states.CharacterItemLoadState

class CharacterLoadStateViewHolder(
    private val binding: CharactersLoadStateItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.btRetry.setOnClickListener {
            retry.invoke()
        }
    }

    fun bind(loadState: LoadState) {
        with(binding) {
            this.loadState = CharacterItemLoadState(
                loadState is LoadState.Loading,
                loadState is LoadState.Error,
                evaluateError(loadState)
            )
            executePendingBindings()
        }
    }

    private fun evaluateError(loadState: LoadState): String? {
        return if (loadState is LoadState.Error) loadState.error.localizedMessage else null
    }
}
