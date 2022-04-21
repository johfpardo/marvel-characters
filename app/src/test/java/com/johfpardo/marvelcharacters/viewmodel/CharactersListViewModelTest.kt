package com.johfpardo.marvelcharacters.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.johfpardo.marvelcharacters.data.model.CharacterSummary
import com.johfpardo.marvelcharacters.testUtils.TestCoroutineRule
import com.johfpardo.marvelcharacters.ui.states.CharacterListUiState
import com.johfpardo.marvelcharacters.usecase.GetCharacters
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CharactersListViewModelTest {

    @MockK
    private lateinit var getCharacters: GetCharacters

    @MockK
    private lateinit var pagingDataObserver: Observer<PagingData<CharacterSummary>>

    @MockK
    private lateinit var uiStateObserver: Observer<CharacterListUiState>

    @MockK
    private lateinit var loadState: CombinedLoadStates

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
        charactersListViewModel = CharactersListViewModel(
            getCharacters,
            testCoroutineRule.testCoroutineDispatcher
        )
        //Given
        every { pagingDataObserver.onChanged(any()) } just Runs
        every { uiStateObserver.onChanged(any()) } just Runs
        //Default values
        every { loadState.refresh } returns LoadState.NotLoading(false)
        every { loadState.source.refresh } returns LoadState.NotLoading(false)
        every { loadState.source.append } returns LoadState.NotLoading(false)
        every { loadState.source.prepend } returns LoadState.NotLoading(true)
        every { loadState.append } returns LoadState.NotLoading(false)
        every { loadState.prepend } returns LoadState.NotLoading(false)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetCharacters() = testCoroutineRule.runBlockingTest {
        //Given
        val dataSlot = slot<PagingData<CharacterSummary>>()
        val character = mockk<CharacterSummary>()
        val testFlow = flow {
            emit(PagingData.from(listOf(character)))
        }
        every { getCharacters.execute() } returns testFlow
        //When
        charactersListViewModel.getCharacters().observeForever(pagingDataObserver)
        //Then
        verify { pagingDataObserver.onChanged(capture(dataSlot)) }
        assertNotNull(dataSlot.captured)
    }

    @Test
    fun handleLoadState_loading_showLoading() {
        //Given
        val uiStateSlot = slot<CharacterListUiState>()
        every { loadState.refresh } returns LoadState.Loading
        every { loadState.source.refresh } returns LoadState.Loading
        charactersListViewModel.uiState.observeForever(uiStateObserver)
        //When
        charactersListViewModel.handleLoadState(loadState, 0)
        //Then
        verify { uiStateObserver.onChanged(capture(uiStateSlot)) }
        assertThat(uiStateSlot.captured.isLoading, `is`(true))
        assertThat(uiStateSlot.captured.isListEmpty, `is`(false))
        assertThat(uiStateSlot.captured.isInitialRetry, `is`(false))
        assertNull(uiStateSlot.captured.errorMessage)
    }

    @Test
    fun handleLoadState_loadedListEmpty_showListEmpty() {
        //Given
        val uiStateSlot = slot<CharacterListUiState>()
        every { loadState.refresh } returns LoadState.NotLoading(true)
        charactersListViewModel.uiState.observeForever(uiStateObserver)
        //When
        charactersListViewModel.handleLoadState(loadState, 0)
        //Then
        verify { uiStateObserver.onChanged(capture(uiStateSlot)) }
        assertThat(uiStateSlot.captured.isLoading, `is`(false))
        assertThat(uiStateSlot.captured.isListEmpty, `is`(true))
        assertThat(uiStateSlot.captured.isInitialRetry, `is`(false))
        assertNull(uiStateSlot.captured.errorMessage)
    }

    @Test
    fun handleLoadState_initialError_showListEmptyErrorAndRetry() {
        //Given
        val uiStateSlot = slot<CharacterListUiState>()
        every { loadState.refresh } returns LoadState.NotLoading(false)
        every { loadState.source.refresh } returns LoadState.Error(Exception(FAKE_ERROR))
        charactersListViewModel.uiState.observeForever(uiStateObserver)
        //When
        charactersListViewModel.handleLoadState(loadState, 0)
        //Then
        verify { uiStateObserver.onChanged(capture(uiStateSlot)) }
        assertThat(uiStateSlot.captured.isLoading, `is`(false))
        assertThat(uiStateSlot.captured.isListEmpty, `is`(true))
        assertThat(uiStateSlot.captured.isInitialRetry, `is`(true))
        assertThat(uiStateSlot.captured.errorMessage, `is`(FAKE_ERROR))
    }

    //error
    @Test
    fun handleLoadState_noInitialError_showError() {
        //Given
        val uiStateSlot = slot<CharacterListUiState>()
        every { loadState.refresh } returns LoadState.Error(Exception(FAKE_ERROR))
        charactersListViewModel.uiState.observeForever(uiStateObserver)
        //When
        charactersListViewModel.handleLoadState(loadState, 20)
        //Then
        verify { uiStateObserver.onChanged(capture(uiStateSlot)) }
        assertThat(uiStateSlot.captured.isLoading, `is`(false))
        assertThat(uiStateSlot.captured.isListEmpty, `is`(false))
        assertThat(uiStateSlot.captured.isInitialRetry, `is`(false))
        assertThat(uiStateSlot.captured.errorMessage, `is`(FAKE_ERROR))
    }

    companion object {
        private const val FAKE_ERROR = "Error"
    }
}
