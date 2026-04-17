package com.hendry.androidpretest.network

import com.hendry.androidpretest.network.ExecutionRequest
import com.hendry.androidpretest.network.ExecutionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("execution")
    suspend fun executeTransaction(
        @Body request: ExecutionRequest
    ): Response<ExecutionResponse>
}