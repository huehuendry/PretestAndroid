package com.hendry.androidpretest.model

import java.io.Serializable

data class Transaction(
    val reffId: String,
    val time: String,
    val nominal: Long,
    val type: TransactionType,
    val status: String
) : Serializable

