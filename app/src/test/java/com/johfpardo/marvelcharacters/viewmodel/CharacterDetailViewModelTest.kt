package com.johfpardo.marvelcharacters.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.johfpardo.marvelcharacters.data.model.CharacterDataContainer
import com.johfpardo.marvelcharacters.data.model.Resource
import com.johfpardo.marvelcharacters.data.repository.CharacterRepository
import com.johfpardo.marvelcharacters.testUtils.TestCoroutineRule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CharacterDetailViewModelTest {

    @MockK
    private lateinit var characterRepository: CharacterRepository

    @MockK
    private lateinit var resourceDataObserver: Observer<Resource<CharacterDataContainer>>

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
        characterDetailViewModel = CharacterDetailViewModel(characterRepository,
            testCoroutineRule.testCoroutineDispatcher)
        every { resourceDataObserver.onChanged(any()) } just Runs
    }

    @Test
    fun getCharacterById_callApi_passSameData() {
        //Given
        val characterData = mockk<CharacterDataContainer>()
        val characterResource = Resource.Success(characterData)
        coEvery { characterRepository.getCharacterById(any()) } returns characterResource
        //When
        characterDetailViewModel.getCharacterById(FAKE_CHARACTER_ID).observeForever(resourceDataObserver)
        //Then
        coVerify {
            characterRepository.getCharacterById(FAKE_CHARACTER_ID)
        }
    }

    @Test
    fun getCharacterById_success_returnData() {
        //Given
        val characterData = mockk<CharacterDataContainer>()
        val characterResource = Resource.Success(characterData)
        val capturedResource = mutableListOf<Resource<CharacterDataContainer>>()
        coEvery { characterRepository.getCharacterById(any()) } returns characterResource
        //When
        characterDetailViewModel.getCharacterById(FAKE_CHARACTER_ID).observeForever(resourceDataObserver)
        //Then
        verify {
            resourceDataObserver.onChanged(capture(capturedResource))
            resourceDataObserver.onChanged(capture(capturedResource))
        }
        assert(capturedResource[0] is Resource.Loading)
        assert(capturedResource[1] is Resource.Success)
        assertThat(capturedResource[1].data, `is`(characterData))
    }

    @Test
    fun getCharacterById_error_returnError() {
        //Given
        val capturedResource = mutableListOf<Resource<CharacterDataContainer>>()
        coEvery { characterRepository.getCharacterById(any()) } throws Exception(FAKE_ERROR_MESSAGE)
        //When
        characterDetailViewModel.getCharacterById(FAKE_CHARACTER_ID).observeForever(resourceDataObserver)
        //Then
        verify {
            resourceDataObserver.onChanged(capture(capturedResource))
            resourceDataObserver.onChanged(capture(capturedResource))
        }
        assert(capturedResource[0] is Resource.Loading)
        assert(capturedResource[1] is Resource.Error)
        assertThat(capturedResource[1].message, `is`(FAKE_ERROR_MESSAGE))
    }

    companion object {
        private const val FAKE_CHARACTER_ID = "31312"
        private const val FAKE_ERROR_MESSAGE = "Error"
    }
}
