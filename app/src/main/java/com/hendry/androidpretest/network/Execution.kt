package com.hendry.androidpretest.network

data class ExecutionRequest(
    val reffId: String,
    val time: String,
    val nominal: Long,
    val type: String
)

data class ExecutionResponse(
    val status: String
)