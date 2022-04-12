package com.johfpardo.marvelcharacters.data.repository

import com.johfpardo.marvelcharacters.data.model.CharacterDataContainer
import com.johfpardo.marvelcharacters.data.model.CharacterDataWrapper
import com.johfpardo.marvelcharacters.data.model.Resource
import com.johfpardo.marvelcharacters.data.remote.CharactersClient
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CharacterRepositoryTest {

    @MockK
    private lateinit var charactersClient: CharactersClient

    @MockK
    private lateinit var characterResponse: Response<CharacterDataWrapper>

    @MockK
    private lateinit var characterData: CharacterDataContainer

    @InjectMockKs
    private lateinit var characterRepository: CharacterRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getCharacterById_callApi_passSameData() = runBlockingTest {
        //Given
        coEvery { charactersClient.getCharacterById(any(), any()) } returns characterResponse
        every { characterResponse.isSuccessful } returns true
        every { characterResponse.body()?.data } returns characterData
        //When
        characterRepository.getCharacterById(FAKE_CHARACTER_ID)
        //Then
        coVerify { charactersClient.getCharacterById(FAKE_CHARACTER_ID, any()) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getCharacterById_success_returnData() = runBlockingTest {
        //Given
        coEvery { charactersClient.getCharacterById(any(), any()) } returns characterResponse
        every { characterResponse.isSuccessful } returns true
        every { characterResponse.body()?.data } returns characterData
        //When
        val result = characterRepository.getCharacterById(FAKE_CHARACTER_ID)
        //Then
        assert(result is Resource.Success)
        assertThat(result.data, `is`(characterData))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getCharacterById_error_returnError() = runBlockingTest {
        //Given
        coEvery { charactersClient.getCharacterById(any(), any()) } returns characterResponse
        every { characterResponse.isSuccessful } returns false
        every { characterResponse.errorBody().toString() } returns FAKE_ERROR_BODY
        //When
        val result = characterRepository.getCharacterById(FAKE_CHARACTER_ID)
        //Then
        assert(result is Resource.Error)
        assertThat(result.message, `is`(FAKE_ERROR_BODY))
    }

    companion object {
        private const val FAKE_CHARACTER_ID = "31312"
        private const val FAKE_ERROR_BODY = "{ \"error\": \"Unauthorized\"}"
    }
}
