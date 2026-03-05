package com.assessment.core.network

suspend fun <T> handleApiCall(apiCall: suspend () -> T): NetworkResult<T> {
    return try {
        val response = apiCall()
        NetworkResult.Success(response)
    } catch (e: Exception) {
        NetworkResult.Error(
            message = e.message.orEmpty()
        )
    }
}
