package com.johfpardo.marvelcharacters.usecase

import com.johfpardo.marvelcharacters.data.model.CharacterDataContainer
import com.johfpardo.marvelcharacters.data.model.Resource
import com.johfpardo.marvelcharacters.data.repository.CharacterRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class GetCharacterByIdTest {

    @MockK
    private lateinit var characterRepository: CharacterRepository

    @InjectMockKs
    private lateinit var getCharacterById: GetCharacterById

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun execute_callApi_passDataCorrectly() = runBlockingTest {
        //Given
        val characterData = mockk<CharacterDataContainer>()
        val characterResource = Resource.Success(characterData)
        coEvery { characterRepository.getCharacterById(any()) } returns characterResource
        //When
        getCharacterById.execute(FAKE_CHARACTER_ID)
        //Then
        coVerify {
            getCharacterById.execute(FAKE_CHARACTER_ID)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun execute_successResponse_returnData() = runBlockingTest {
        //Given
        val characterData = mockk<CharacterDataContainer>()
        val characterResource = Resource.Success(characterData)
        coEvery { characterRepository.getCharacterById(any()) } returns characterResource
        //When
        val result = getCharacterById.execute(FAKE_CHARACTER_ID)
        //Then
        assert(result is Resource.Success)
        assertThat(result.data, `is`(characterData))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun execute_errorResponse_returnError() = runBlockingTest {
        //Given
        val errorResource = Resource.Error<CharacterDataContainer>(FAKE_ERROR_MESSAGE)
        coEvery { characterRepository.getCharacterById(any()) } returns errorResource
        //When
        val result = getCharacterById.execute(FAKE_CHARACTER_ID)
        //Then
        assert(result is Resource.Error)
        assertThat(result.message, `is`(FAKE_ERROR_MESSAGE))
    }

    companion object {
        private const val FAKE_CHARACTER_ID = "31312"
        private const val FAKE_ERROR_MESSAGE = "Error"
    }
}
