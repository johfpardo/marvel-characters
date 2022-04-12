package com.johfpardo.marvelcharacters.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import com.johfpardo.marvelcharacters.data.model.Character
import com.johfpardo.marvelcharacters.data.repository.CharactersRepository
import com.johfpardo.marvelcharacters.testUtils.TestCoroutineRule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CharactersListViewModelTest {

    @MockK
    private lateinit var charactersRepository: CharactersRepository

    @MockK
    private lateinit var pagingDataObserver: Observer<PagingData<Character>>

    private lateinit var charactersListViewModel: CharactersListViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        charactersListViewModel = CharactersListViewModel(charactersRepository,
            testCoroutineRule.testCoroutineDispatcher)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetCharacters() = testCoroutineRule.runBlockingTest {
        //Given
        val dataSlot = slot<PagingData<Character>>()
        val character = mockk<Character>()
        val testFlow = flow {
            emit(PagingData.from(listOf(character)))
        }
        every { charactersRepository.getCharacters() } returns testFlow
        //When
        charactersListViewModel.getCharacters().observeForever(pagingDataObserver)
        //Then
        verify { pagingDataObserver.onChanged(capture(dataSlot)) }
        assertNotNull(dataSlot.captured)
    }
}
