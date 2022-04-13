package com.johfpardo.marvelcharacters.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.johfpardo.marvelcharacters.R
import com.johfpardo.marvelcharacters.di.AppComponentProvider
import com.johfpardo.marvelcharacters.ui.adapters.CharacterLoadStateAdapter
import com.johfpardo.marvelcharacters.ui.adapters.CharactersAdapter
import com.johfpardo.marvelcharacters.ui.adapters.vh.CharactersViewHolder
import com.johfpardo.marvelcharacters.utils.ext.getViewModel
import com.johfpardo.marvelcharacters.viewmodel.CharactersListViewModel
import com.johfpardo.marvelcharacters.viewmodel.CharactersListViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [CharactersListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CharactersListFragment : Fragment(), CharactersViewHolder.CharacterItemListener {

    @Inject
    lateinit var viewModelFactory: CharactersListViewModelFactory
    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        getViewModel<CharactersListViewModel>(viewModelFactory)
    }

    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var btInitialRetry: Button
    private lateinit var adapter: CharactersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as AppComponentProvider).getAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_characters_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = view.findViewById(R.id.progress_circular)
        recyclerView = view.findViewById(R.id.characters_recycler)
        btInitialRetry = view.findViewById(R.id.bt_initial_retry)

        adapter = CharactersAdapter(this)
        recyclerView.adapter =
            adapter.withLoadStateFooter(CharacterLoadStateAdapter { adapter.retry() })
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        btInitialRetry.setOnClickListener { adapter.retry() }

        viewModel.getCharacters().observe(viewLifecycleOwner, {
            it?.let {
                viewLifecycleOwner.lifecycleScope.launch {
                    adapter.submitData(it)
                }
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
                recyclerView.visibility = if (!isListEmpty) View.VISIBLE else View.GONE
                progressBar.visibility =
                    if (loadState.source.refresh is LoadState.Loading) View.VISIBLE else View.GONE
                btInitialRetry.visibility =
                    if (loadState.source.refresh is LoadState.Error) View.VISIBLE else View.GONE

                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    showError("${it.error}")
                }
            }
        }
    }

    override fun onItemClicked(characterId: Int?) {
        characterId?.let {
            val transaction = parentFragmentManager.beginTransaction()
            with(transaction) {
                setCustomAnimations(
                    R.anim.slide_in_from_right, R.anim.slide_out_to_left,
                    R.anim.slide_in_from_left, R.anim.slide_out_to_right
                )
                add(R.id.fragment_container, CharacterDetailFragment.newInstance(it.toString()))
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment CharactersListFragment.
         */
        @JvmStatic
        fun newInstance() = CharactersListFragment()
    }
}
