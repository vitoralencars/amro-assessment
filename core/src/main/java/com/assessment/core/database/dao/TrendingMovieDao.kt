package com.assessment.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.assessment.core.database.entity.TrendingMovieEntity

@Dao
interface TrendingMovieDao {

    @Query("""
        SELECT * FROM trending_movies
        WHERE (:genreId = -1 OR genreIds LIKE '%,' || :genreId || ',%')
        ORDER BY popularity DESC
    """)
    fun pagingByPopularity(genreId: Int): PagingSource<Int, TrendingMovieEntity>

    @Query("""
        SELECT * FROM trending_movies
        WHERE (:genreId = -1 OR genreIds LIKE '%,' || :genreId || ',%')
        ORDER BY LOWER(title) ASC
    """)
    fun pagingByTitle(genreId: Int): PagingSource<Int, TrendingMovieEntity>

    @Query("""
        SELECT * FROM trending_movies
        WHERE (:genreId = -1 OR genreIds LIKE '%,' || :genreId || ',%')
        ORDER BY releaseDate DESC
    """)
    fun pagingByReleaseDateDesc(genreId: Int): PagingSource<Int, TrendingMovieEntity>

    @Query("""
        SELECT * FROM trending_movies
        WHERE (:genreId = -1 OR genreIds LIKE '%,' || :genreId || ',%')
        ORDER BY releaseDate ASC
    """)
    fun pagingByReleaseDateAsc(genreId: Int): PagingSource<Int, TrendingMovieEntity>

    @Query("SELECT COUNT(*) FROM trending_movies")
    suspend fun count(): Int

    @Upsert
    suspend fun upsertAll(items: List<TrendingMovieEntity>)

    @Query("DELETE FROM trending_movies")
    suspend fun clearAll()
}
