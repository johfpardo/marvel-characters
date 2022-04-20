package com.johfpardo.marvelcharacters.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.johfpardo.marvelcharacters.data.model.Character
import com.johfpardo.marvelcharacters.data.model.CharacterDataContainer
import com.johfpardo.marvelcharacters.data.model.Resource
import com.johfpardo.marvelcharacters.testUtils.TestCoroutineRule
import com.johfpardo.marvelcharacters.ui.states.CharacterDetailUiState
import com.johfpardo.marvelcharacters.usecase.GetCharacterById
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CharacterDetailViewModelTest {

    @MockK
    private lateinit var getCharacterById: GetCharacterById

    @MockK
    private lateinit var uiStateObserver: Observer<CharacterDetailUiState>

    private lateinit var characterDetailViewModel: CharacterDetailViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        characterDetailViewModel = CharacterDetailViewModel(
            getCharacterById,
            testCoroutineRule.testCoroutineDispatcher
        )
        every { uiStateObserver.onChanged(any()) } just Runs
    }

    @Test
    fun getCharacterById_callApi_passSameData() {
        //Given
        val characterData = mockk<CharacterDataContainer>()
        val characterResource = Resource.Success(characterData)
        coEvery { getCharacterById.execute(any()) } returns characterResource
        characterDetailViewModel.uiState.observeForever(uiStateObserver)
        //When
        characterDetailViewModel.getCharacterById(FAKE_CHARACTER_ID)
        //Then
        coVerify {
            getCharacterById.execute(FAKE_CHARACTER_ID)
        }
    }

    @Test
    fun getCharacterById_success_returnData() {
        //Given
        val characterData = mockk<CharacterDataContainer>()
        val character = mockk<Character>()
        val characterResource = Resource.Success(characterData)
        val capturedUiState = mutableListOf<CharacterDetailUiState>()
        every { characterData.results[0] } returns character
        coEvery { getCharacterById.execute(any()) } returns characterResource
        characterDetailViewModel.uiState.observeForever(uiStateObserver)
        //When
        characterDetailViewModel.getCharacterById(FAKE_CHARACTER_ID)
        //Then
        verify {
            uiStateObserver.onChanged(capture(capturedUiState))
            uiStateObserver.onChanged(capture(capturedUiState))
        }
        assertThat(capturedUiState[0].isLoading, `is`(true))
        assertNotNull(capturedUiState[1].character)
        assertNull(capturedUiState[1].errorMessage)
        assertThat(capturedUiState[1].character, `is`(character))
    }

    @Test
    fun getCharacterById_error_returnError() {
        //Given
        val capturedUiState = mutableListOf<CharacterDetailUiState>()
        val errorResource = Resource.Error<CharacterDataContainer>(FAKE_ERROR_MESSAGE)
        coEvery { getCharacterById.execute(any()) } returns errorResource
        characterDetailViewModel.uiState.observeForever(uiStateObserver)
        //When
        characterDetailViewModel.getCharacterById(FAKE_CHARACTER_ID)
        //Then
        verify {
            uiStateObserver.onChanged(capture(capturedUiState))
            uiStateObserver.onChanged(capture(capturedUiState))
        }
        assertThat(capturedUiState[0].isLoading, `is`(true))
        assertNotNull(capturedUiState[1].errorMessage)
        assertNull(capturedUiState[1].character)
        assertThat(capturedUiState[1].errorMessage, `is`(FAKE_ERROR_MESSAGE))
    }

    @Test
    fun getCharacterById_exceptionOccurs_returnError() {
        //Given
        val capturedUiState = mutableListOf<CharacterDetailUiState>()
        coEvery { getCharacterById.execute(any()) } throws Exception(FAKE_ERROR_MESSAGE)
        characterDetailViewModel.uiState.observeForever(uiStateObserver)
        //When
        characterDetailViewModel.getCharacterById(FAKE_CHARACTER_ID)
        //Then
        verify {
            uiStateObserver.onChanged(capture(capturedUiState))
            uiStateObserver.onChanged(capture(capturedUiState))
        }
        assertThat(capturedUiState[0].isLoading, `is`(true))
        assertNotNull(capturedUiState[1].errorMessage)
        assertNull(capturedUiState[1].character)
        assertThat(capturedUiState[1].errorMessage, `is`(FAKE_ERROR_MESSAGE))
    }

    companion object {
        private const val FAKE_CHARACTER_ID = "31312"
        private const val FAKE_ERROR_MESSAGE = "Error"
    }
}
