package com.assessment.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val navigator: Navigator,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val navJson = Json {
        serializersModule = navKeyModule
        classDiscriminator = "type"
    }

    init {
        val savedJson = savedStateHandle.get<String>("backstack_json")
        if (savedJson != null) {
            try {
                val list = navJson.decodeFromString<List<NavKey>>(savedJson)
                navigator.restoreState(list)
            } catch (e: Exception) {}
        }

        viewModelScope.launch {
            navigator.backStack.collect { list ->
                val jsonString = navJson.encodeToString(list)
                savedStateHandle["backstack_json"] = jsonString
            }
        }
    }
}
