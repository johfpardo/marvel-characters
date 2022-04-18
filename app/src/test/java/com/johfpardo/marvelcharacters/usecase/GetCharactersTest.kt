package com.johfpardo.marvelcharacters.usecase

import androidx.paging.PagingData
import com.johfpardo.marvelcharacters.data.model.Character
import com.johfpardo.marvelcharacters.data.repository.CharactersRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class GetCharactersTest {

    @MockK
    private lateinit var charactersRepository: CharactersRepository

    @InjectMockKs
    private lateinit var getCharacters: GetCharacters

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun execute_callApiCorrectly() {
        //Given
        val character = mockk<Character>()
        val testFlow = flow {
            emit(PagingData.from(listOf(character)))
        }
        every { charactersRepository.getCharacters() } returns testFlow
        //When
        getCharacters.execute()
        //Then
        verify { charactersRepository.getCharacters() }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun execute_callApi_returnExpectedData() = runBlockingTest {
        //Given
        val character = mockk<Character>()
        val testFlow = flow {
            emit(PagingData.from(listOf(character)))
        }
        every { charactersRepository.getCharacters() } returns testFlow
        //When
        val result = getCharacters.execute()
        //Then
        val pagingData = result.first()
        assertNotNull(pagingData)
    }
}