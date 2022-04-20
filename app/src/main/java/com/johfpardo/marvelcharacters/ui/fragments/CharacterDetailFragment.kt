package com.johfpardo.marvelcharacters.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.johfpardo.marvelcharacters.databinding.FragmentCharacterDetailBinding
import com.johfpardo.marvelcharacters.di.AppComponentProvider
import com.johfpardo.marvelcharacters.ui.adapters.CharacterDetailAdapter
import com.johfpardo.marvelcharacters.utils.ext.getViewModel
import com.johfpardo.marvelcharacters.viewmodel.CharacterDetailViewModel
import com.johfpardo.marvelcharacters.viewmodel.CharacterDetailViewModelFactory
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [CharacterDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CharacterDetailFragment : Fragment() {

    private var characterId: String? = null

    @Inject
    lateinit var viewModelFactory: CharacterDetailViewModelFactory
    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        getViewModel<CharacterDetailViewModel>(viewModelFactory)
    }

    private lateinit var adapter: CharacterDetailAdapter
    private lateinit var binding: FragmentCharacterDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as AppComponentProvider).getAppComponent().inject(this)
        arguments?.let {
            characterId = it.getString(ARG_CHARACTER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharacterDetailBinding.inflate(inflater, container, false)
        adapter = CharacterDetailAdapter(arrayListOf())

        binding.rvDetailList.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        characterId?.let { characterId ->
            viewModel.getCharacterById(characterId)
        }

        viewModel.uiState.observe(viewLifecycleOwner, { uiState ->
            binding.uiState = uiState
            uiState.errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
            uiState.character?.let {
                adapter.addItems(it.getDetailItems())
            }
        })
    }

    companion object {

        private const val ARG_CHARACTER_ID = "characterId"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param characterId The id of the character.
         * @return A new instance of fragment CharacterDetailFragment.
         */
        @JvmStatic
        fun newInstance(characterId: String) =
            CharacterDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CHARACTER_ID, characterId)
                }
            }
    }
}
