package com.johfpardo.marvelcharacters.ui.adapters.vh

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.johfpardo.marvelcharacters.R

class CharacterLoadStateViewHolder(
    itemView: View,
    retry: () -> Unit
) : RecyclerView.ViewHolder(itemView) {
    private val tvErrorMsg : TextView = itemView.findViewById(R.id.tv_error_msg)
    private val progressBar : ProgressBar = itemView.findViewById(R.id.progress_bar)
    private val btRetry: Button = itemView.findViewById(R.id.bt_retry)

    init {
        btRetry.setOnClickListener {
            retry.invoke()
        }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            tvErrorMsg.text = loadState.error.localizedMessage
        }
        tvErrorMsg.visibility = if (loadState is LoadState.Error) View.VISIBLE else View.GONE
        progressBar.visibility = if (loadState is LoadState.Loading) View.VISIBLE else View.GONE
        btRetry.visibility = if (loadState is LoadState.Error) View.VISIBLE else View.GONE
    }

}
