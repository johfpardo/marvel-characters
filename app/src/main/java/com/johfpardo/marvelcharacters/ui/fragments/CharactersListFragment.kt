package com.johfpardo.marvelcharacters.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.johfpardo.marvelcharacters.R
import com.johfpardo.marvelcharacters.data.model.CharacterDataContainer
import com.johfpardo.marvelcharacters.data.model.Resource
import com.johfpardo.marvelcharacters.di.AppComponentProvider
import com.johfpardo.marvelcharacters.ui.adapters.CharactersAdapter
import com.johfpardo.marvelcharacters.utils.ext.getViewModel
import com.johfpardo.marvelcharacters.viewmodel.CharactersListViewModel
import com.johfpardo.marvelcharacters.viewmodel.CharactersListViewModelFactory
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [CharactersListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CharactersListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: CharactersListViewModelFactory
    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        getViewModel<CharactersListViewModel>(viewModelFactory)
    }

    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: CharactersAdapter

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
        adapter = CharactersAdapter(arrayListOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getCharacters().observe(viewLifecycleOwner, {
            it?.let {
                handleResponse(it)
            }
        })
    }

    private fun handleResponse(resource: Resource<CharacterDataContainer>) {
        when (resource) {
            is Resource.Loading -> {
                progressBar.visibility = View.VISIBLE
            }
            is Resource.Success -> {
                progressBar.visibility = View.GONE
                resource.data?.results?.let {
                    adapter.addCharacters(it)
                }
            }
            is Resource.Error -> {
                progressBar.visibility = View.GONE
                showError(resource.message ?: "Unknown error")
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
