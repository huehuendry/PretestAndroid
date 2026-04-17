package com.hendry.androidpretest.viewmodel

import com.hendry.androidpretest.model.TransactionType

data class GatherState(
    val type: TransactionType = TransactionType.TRANSFER,
    val amount: String = "",
    val error: String? = null,
    val isLoading: Boolean = false,
    val isButtonEnabled: Boolean = false
)