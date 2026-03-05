package com.assessment.core.network

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    private val accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4YTIzMjY3YThhNDFhZDM5Nzc2YTdlMmY3OWQzZTkwMSIsIm5iZiI6MTU2MjA3NDQyNi4zMDA5OTk5LCJzdWIiOiI1ZDFiNWQzYTU1YjBjMDMyNGI2OWRlOTUiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.onCVV7POGShiMC1_BBaFqWqv3ab-tP_imyHKetYKZZo"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .addHeader("Accept", "application/json")
            .build()

        return chain.proceed(newRequest)
    }
}
