package com.assessment.movies.di

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.assessment.movies.presentation.details.ui.MovieDetailsScreen
import com.assessment.movies.presentation.trending.ui.TrendingMoviesScreen
import com.assessment.navigation.MovieDetailsNavKey
import com.assessment.navigation.Navigator
import com.assessment.navigation.TrendingMoviesNavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import kotlin.jvm.JvmSuppressWildcards

@Module
@InstallIn(ActivityRetainedComponent::class)
object MoviesNavigationModule {

    @IntoSet
    @Provides
    fun provideTrendingMoviesEntryBuilder(
        navigator: Navigator,
    ): @JvmSuppressWildcards (EntryProviderScope<NavKey>.() -> Unit) = {
        entry<TrendingMoviesNavKey> { _ ->
            TrendingMoviesScreen(
                onNavigateToMovieDetails = { movieId ->
                    navigator.navTo(MovieDetailsNavKey(
                        movieId = movieId,
                    ))
                },
            )
        }
    }

    @IntoSet
    @Provides
    fun provideMovieDetailEntryBuilder(
        navigator: Navigator,
    ): @JvmSuppressWildcards (EntryProviderScope<NavKey>.() -> Unit) = {
        entry<MovieDetailsNavKey> { key ->
            MovieDetailsScreen(
                movieId = key.movieId,
                onBackClick = { navigator.navBack() },
            )
        }
    }
}
