package com.johfpardo.marvelcharacters.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.johfpardo.marvelcharacters.data.model.Character
import com.johfpardo.marvelcharacters.data.remote.CharactersClient
import com.johfpardo.marvelcharacters.utils.DateUtils.currentTimestamp

class CharactersPagingSource(
    private val charactersClient: CharactersClient
) : PagingSource<Int, Character>() {
    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(state.config.pageSize)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(state.config.pageSize)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val offset = params.key ?: INITIAL_OFFSET
        return try {
            val response = charactersClient.getCharacters(currentTimestamp(), params.loadSize, offset)
            if (response.isSuccessful) {
                val data = response.body()?.data
                val total = data?.total ?: 0
                val prevKey = if (offset == INITIAL_OFFSET) {
                    null
                } else {
                    offset - params.loadSize
                }
                val nextKey = if (total > offset + params.loadSize) {
                    offset + params.loadSize
                } else {
                    null
                }
                LoadResult.Page(
                    data?.results ?: emptyList(),
                    prevKey,
                    nextKey
                )
            } else {
                LoadResult.Error(Exception(response.errorBody()?.toString()))
            }
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        private const val INITIAL_OFFSET = 0
    }
}
