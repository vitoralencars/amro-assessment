package com.assessment.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Serializable
data object TrendingMoviesNavKey : NavKey

@Serializable
data class MovieDetailsNavKey(val movieId: Long) : NavKey

internal val navKeyModule = SerializersModule {
    polymorphic(NavKey::class) {
        subclass(TrendingMoviesNavKey::class, TrendingMoviesNavKey.serializer())
        subclass(MovieDetailsNavKey::class, MovieDetailsNavKey.serializer())
    }
}
