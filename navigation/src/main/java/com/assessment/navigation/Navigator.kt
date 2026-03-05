package com.assessment.navigation

import androidx.navigation3.runtime.NavKey
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@ActivityRetainedScoped
class Navigator @Inject constructor() {

    private val _backStack = MutableStateFlow<List<NavKey>>(listOf(TrendingMoviesNavKey))
    val backStack = _backStack.asStateFlow()

    fun navTo(destination: NavKey) {
        _backStack.update { it + destination }
    }

    fun navBack() {
        _backStack.update { if (it.size > 1) it.dropLast(1) else it }
    }

    fun restoreState(savedList: List<NavKey>) {
        _backStack.value = savedList
    }
}
