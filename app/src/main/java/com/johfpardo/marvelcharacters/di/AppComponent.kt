package com.johfpardo.marvelcharacters.di

import com.johfpardo.marvelcharacters.ui.fragments.CharacterDetailFragment
import com.johfpardo.marvelcharacters.ui.fragments.CharactersListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ServiceModule::class])
interface AppComponent {
    fun inject(charactersListFragment: CharactersListFragment)

    fun inject(characterDetailFragment: CharacterDetailFragment)
}
