@file:OptIn(ExperimentalPagingApi::class)

package com.assessment.movies.data.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.assessment.core.database.AmroDatabase
import com.assessment.core.database.entity.TrendingMovieEntity
import com.assessment.movies.data.service.MoviesApiService
import javax.inject.Inject

class TrendingMoviesRemoteMediator @Inject constructor (
    private val apiService: MoviesApiService,
    private val database: AmroDatabase,
) : RemoteMediator<Int, TrendingMovieEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TrendingMovieEntity>,
    ): MediatorResult {
        val trendingMovieDao = database.trendingMovieDao()
        val currentCount = trendingMovieDao.count()

        if (loadType == LoadType.APPEND) {
            if (currentCount >= MAX_ITEMS) {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val loaded = state.pages.sumOf { it.data.size }
                (loaded / state.config.pageSize) + 1
            }
        }

        return try {
            val response = apiService.getTrendingMovies(page = page)
            val entities = response.results.map { it.toEntity() }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    trendingMovieDao.clearAll()
                }
                trendingMovieDao.upsertAll(entities)
            }

            MediatorResult.Success(
                endOfPaginationReached = response.results.isEmpty() || trendingMovieDao.count() >= MAX_ITEMS,
            )
        } catch (e: Exception) {
            if (currentCount > 0) {
                MediatorResult.Success(endOfPaginationReached = true)
            } else {
                MediatorResult.Error(e)
            }
        }
    }

    private companion object {
        const val MAX_ITEMS = 100
    }
}
