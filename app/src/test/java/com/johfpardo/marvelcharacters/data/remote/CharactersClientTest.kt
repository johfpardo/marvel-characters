package com.johfpardo.marvelcharacters.data.remote

import com.johfpardo.marvelcharacters.data.model.CharacterDataWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CharactersClientTest {

    @MockK
    private lateinit var charactersService: CharactersService

    @MockK
    private lateinit var characterResponse: Response<CharacterDataWrapper>

    @InjectMockKs
    private lateinit var charactersClient: CharactersClient

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getCharacters_callApi_passSameData() = runBlockingTest {
        //Given
        coEvery { charactersService.getCharacters(any(), any()) } returns characterResponse
        //When
        val result = charactersClient.getCharacters(FAKE_LIMIT, FAKE_OFFSET)
        //Then
        coVerify { charactersService.getCharacters(FAKE_LIMIT, FAKE_OFFSET) }
        assertThat(result, `is`(characterResponse))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getCharacterById_callApi_passSameData() = runBlockingTest {
        //Given
        coEvery { charactersService.getCharacterById(any()) } returns characterResponse
        //When
        val result = charactersClient.getCharacterById(FAKE_CHARACTER_ID)
        //Then
        coVerify { charactersService.getCharacterById(FAKE_CHARACTER_ID) }
        assertThat(result, `is`(characterResponse))
    }

    companion object {
        private const val FAKE_LIMIT = 20
        private const val FAKE_OFFSET = 20
        private const val FAKE_CHARACTER_ID = "131231"
    }
}
