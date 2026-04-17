package com.hendry.androidpretest.viewmodel

import androidx.lifecycle.ViewModel
import com.hendry.androidpretest.data.TransactionRepository

class HomeViewModel : ViewModel() {

    val balance = TransactionRepository.balance
    val transactions = TransactionRepository.transactions
}