package com.johfpardo.marvelcharacters.data.repository.paging

import android.accounts.NetworkErrorException
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.johfpardo.marvelcharacters.data.model.Character
import com.johfpardo.marvelcharacters.data.model.CharacterDataContainer
import com.johfpardo.marvelcharacters.data.model.CharacterDataWrapper
import com.johfpardo.marvelcharacters.data.remote.CharactersClient
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CharactersPagingSourceTest {

    @MockK
    private lateinit var charactersClient: CharactersClient

    @MockK
    private lateinit var loadParams: PagingSource.LoadParams<Int>

    @MockK
    private lateinit var pagingState: PagingState<Int, Character>

    @MockK
    private lateinit var response: Response<CharacterDataWrapper>

    @MockK
    private lateinit var characterData: CharacterDataContainer

    @MockK
    private lateinit var characters: List<Character>

    @InjectMockKs
    private lateinit var charactersPagingSource: CharactersPagingSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun getRefreshKey_notAnchorPosition_shouldReturnNull() {
        //Given
        every { pagingState.anchorPosition } returns null
        //When
        val result = charactersPagingSource.getRefreshKey(pagingState)
        //Then
        assertNull(result)
    }

    @Test
    fun getRefreshKey_prevKeyExists_shouldReturnNewNextKey() {
        //Given
        every { pagingState.anchorPosition } returns 12
        every { pagingState.closestPageToPosition(any())?.prevKey } returns FAKE_PREV_KEY
        every { pagingState.config } returns PagingConfig(FAKE_PAGE_SIZE)

        //When
        val result = charactersPagingSource.getRefreshKey(pagingState)
        //Then
        assertThat(result, `is`(FAKE_PREV_KEY + FAKE_PAGE_SIZE))
    }

    @Test
    fun getRefreshKey_prevKeyNotExistAndNextKeyExists_shouldReturnNewPrevKey() {
        //Given
        every { pagingState.anchorPosition } returns 2
        every { pagingState.closestPageToPosition(any())?.prevKey } returns null
        every { pagingState.closestPageToPosition(any())?.nextKey } returns FAKE_NEXT_KEY
        every { pagingState.config } returns PagingConfig(FAKE_PAGE_SIZE)

        //When
        val result = charactersPagingSource.getRefreshKey(pagingState)
        //Then
        assertThat(result, `is`(FAKE_NEXT_KEY - FAKE_PAGE_SIZE))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun load_successfulNoData_shouldReturnEmpty() = runBlockingTest {
        //Given
        every { loadParams.key } returns 0
        every { loadParams.loadSize } returns FAKE_PAGE_SIZE
        every { response.isSuccessful } returns true
        every { response.body()?.data } returns characterData
        every { characterData.total } returns 0
        every { characterData.results } returns emptyList()
        coEvery { charactersClient.getCharacters(any(), any()) } returns response
        //When
        val result = charactersPagingSource.load(loadParams)
        //Then
        coVerify { charactersClient.getCharacters(FAKE_PAGE_SIZE, 0) }
        assert(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page<Int, Character>
        assertThat(page.data.isEmpty(), `is`(true))
        assertNull(page.prevKey)
        assertNull(page.nextKey)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun load_successfulFirstCall_shouldReturnData() = runBlockingTest {
        //Given
        every { loadParams.key } returns 0
        every { loadParams.loadSize } returns FAKE_PAGE_SIZE
        every { response.isSuccessful } returns true
        every { response.body()?.data } returns characterData
        every { characterData.total } returns FAKE_TOTAL
        every { characterData.results } returns characters
        coEvery { charactersClient.getCharacters(any(), any()) } returns response
        //When
        val result = charactersPagingSource.load(loadParams)
        //Then
        coVerify { charactersClient.getCharacters(FAKE_PAGE_SIZE, 0) }
        assert(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page<Int, Character>
        assertThat(page.data, `is`(characters))
        assertNull(page.prevKey)
        assertThat(page.nextKey, `is`(FAKE_PAGE_SIZE))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun load_successfulSecondCall_shouldReturnData() = runBlockingTest {
        //Given
        every { loadParams.key } returns FAKE_SECOND_KEY
        every { loadParams.loadSize } returns FAKE_PAGE_SIZE
        every { response.isSuccessful } returns true
        every { response.body()?.data } returns characterData
        every { characterData.total } returns FAKE_TOTAL
        every { characterData.results } returns characters
        coEvery { charactersClient.getCharacters(any(), any()) } returns response
        //When
        val result = charactersPagingSource.load(loadParams)
        //Then
        coVerify { charactersClient.getCharacters(FAKE_PAGE_SIZE, FAKE_SECOND_KEY) }
        assert(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page<Int, Character>
        assertThat(page.data, `is`(characters))
        assertThat(page.prevKey, `is`(FAKE_SECOND_KEY - FAKE_PAGE_SIZE))
        assertThat(page.nextKey, `is`(FAKE_SECOND_KEY + FAKE_PAGE_SIZE))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun load_successfulLastCall_shouldReturnData() = runBlockingTest {
        //Given
        every { loadParams.key } returns FAKE_LAST_KEY
        every { loadParams.loadSize } returns FAKE_PAGE_SIZE
        every { response.isSuccessful } returns true
        every { response.body()?.data } returns characterData
        every { characterData.total } returns FAKE_TOTAL
        every { characterData.results } returns characters
        coEvery { charactersClient.getCharacters(any(), any()) } returns response
        //When
        val result = charactersPagingSource.load(loadParams)
        //Then
        coVerify { charactersClient.getCharacters(FAKE_PAGE_SIZE, FAKE_LAST_KEY) }
        assert(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page<Int, Character>
        assertThat(page.data, `is`(characters))
        assertThat(page.prevKey, `is`(FAKE_LAST_KEY - FAKE_PAGE_SIZE))
        assertNull(page.nextKey)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun load_networkError_shouldReturnError() = runBlockingTest {
        //Given
        every { loadParams.key } returns 0
        every { loadParams.loadSize } returns FAKE_PAGE_SIZE
        coEvery {
            charactersClient.getCharacters(any(), any())
        } throws NetworkErrorException("NetworkError")
        //When
        val result = charactersPagingSource.load(loadParams)
        //Then
        assert(result is PagingSource.LoadResult.Error)
        val error = result as PagingSource.LoadResult.Error
        assert(error.throwable is NetworkErrorException)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun load_unauthorized_shouldReturnError() = runBlockingTest {
        //Given
        every { loadParams.key } returns 0
        every { loadParams.loadSize } returns FAKE_PAGE_SIZE
        every { response.isSuccessful } returns false
        every { response.errorBody()?.toString() } returns FAKE_ERROR_BODY
        coEvery { charactersClient.getCharacters(any(), any()) } returns response
        //When
        val result = charactersPagingSource.load(loadParams)
        //Then
        assert(result is PagingSource.LoadResult.Error)
        val error = result as PagingSource.LoadResult.Error
        assert(error.throwable is Exception)
        val exception = error.throwable as Exception
        assertThat(exception.localizedMessage, `is`(FAKE_ERROR_BODY))
    }

    companion object {
        private const val FAKE_PAGE_SIZE = 20
        private const val FAKE_PREV_KEY = 0
        private const val FAKE_NEXT_KEY = 20
        private const val FAKE_SECOND_KEY = 20
        private const val FAKE_LAST_KEY = 40
        private const val FAKE_TOTAL = 60
        private const val FAKE_ERROR_BODY = "{ \"error\": \"Unauthorized\"}"
    }
}
