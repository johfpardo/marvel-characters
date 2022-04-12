package com.johfpardo.marvelcharacters.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.johfpardo.marvelcharacters.R
import com.johfpardo.marvelcharacters.data.model.Character
import com.johfpardo.marvelcharacters.data.model.CharacterDataContainer
import com.johfpardo.marvelcharacters.data.model.Resource
import com.johfpardo.marvelcharacters.di.AppComponentProvider
import com.johfpardo.marvelcharacters.ui.adapters.CharacterDetailAdapter
import com.johfpardo.marvelcharacters.utils.ext.getViewModel
import com.johfpardo.marvelcharacters.viewmodel.CharacterDetailViewModel
import com.johfpardo.marvelcharacters.viewmodel.CharacterDetailViewModelFactory
import com.squareup.picasso.Picasso
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

    private lateinit var ivAvatar: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvDescription: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: CharacterDetailAdapter

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
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_character_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivAvatar = view.findViewById(R.id.iv_detail_avatar)
        tvName = view.findViewById(R.id.tv_detail_name)
        tvDescription = view.findViewById(R.id.tv_detail_description)
        recyclerView = view.findViewById(R.id.rv_detail_list)
        progressBar = view.findViewById(R.id.progress_detail_circular)

        adapter = CharacterDetailAdapter(arrayListOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        characterId?.let { characterId ->
            viewModel.getCharacterById(characterId).observe(viewLifecycleOwner, {
                it.let {
                    handleResponse(it)
                }
            })
        }
    }

    private fun handleResponse(resource: Resource<CharacterDataContainer>) {
        when (resource) {
            is Resource.Loading -> {
                progressBar.visibility = View.VISIBLE
            }
            is Resource.Success -> {
                progressBar.visibility = View.GONE
                showInfo(resource.data?.results?.get(0))
            }
            is Resource.Error -> {
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), resource.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showInfo(character: Character?) {
        character?.let {
            tvName.text = it.name
            tvDescription.text = it.description
            Picasso.get().load(it.thumbnail?.fullPath).into(ivAvatar)
            adapter.addItems(it.getDetailItems())
        }
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
