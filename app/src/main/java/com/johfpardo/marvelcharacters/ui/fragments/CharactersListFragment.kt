package com.johfpardo.marvelcharacters.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.johfpardo.marvelcharacters.R
import com.johfpardo.marvelcharacters.databinding.FragmentCharactersListBinding
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

    private lateinit var adapter: CharactersAdapter
    private lateinit var binding: FragmentCharactersListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as AppComponentProvider).getAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharactersListBinding.inflate(inflater, container, false)
        adapter = CharactersAdapter(this)
        binding.charactersRecycler.adapter =
            adapter.withLoadStateFooter(CharacterLoadStateAdapter { adapter.retry() })

        binding.btInitialRetry.setOnClickListener {
            adapter.retry()
        }
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCharacters().observe(viewLifecycleOwner, {
            it?.let {
                viewLifecycleOwner.lifecycleScope.launch {
                    adapter.submitData(it)
                }
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                viewModel.handleLoadState(loadState, adapter.itemCount)
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner, { uiState ->
            uiState.errorMessage?.let {
                showError(it)
            }
        })
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
