package com.johfpardo.marvelcharacters.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.johfpardo.marvelcharacters.data.model.Character
import com.johfpardo.marvelcharacters.data.repository.paging.CharactersPagingSource
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class CharactersRepositoryTest {

    @MockK
    private lateinit var charactersPagingSource: CharactersPagingSource

    @InjectMockKs
    private lateinit var charactersRepository: CharactersRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkConstructor(Pager::class)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun getCharacters_testFunctionality() = runBlockingTest {
        //Given
        val character = mockk<Character>()
        val testFlow = flow {
            emit(PagingData.from(listOf(character)))
        }
        every { anyConstructed<Pager<Int, Character>>().flow } returns testFlow
        //When
        val result = charactersRepository.getCharacters()
        //Then
        verify { anyConstructed<Pager<Int, Character>>().flow }
        val pagingData = result.first()
        assertNotNull(pagingData)
    }
}
