package com.hendry.androidpretest.viewmodel

import com.hendry.androidpretest.model.Transaction

sealed class GatherEvent {
    data class NavigateToReceipt(val transaction: Transaction) : GatherEvent()
}