package com.hendry.androidpretest.data

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.hendry.androidpretest.model.Transaction
import com.hendry.androidpretest.model.TransactionType

object TransactionRepository {

    private var localStorage: LocalStorage? = null

    fun init(context: Context) {
        localStorage = LocalStorage(context)

        val savedBalance = localStorage?.getBalance() ?: 500_000L
        val savedTransactions = localStorage?.getTransactions() ?: emptyList()

        Log.d("Debug ", "Balance: $savedBalance")
        Log.d("Debug", "Transaction: ${savedTransactions.size}")

        _balance.value = localStorage?.getBalance() ?: 500_000L
        _transactions.value = localStorage?.getTransactions() ?: emptyList()
    }

    private val _balance = MutableStateFlow(0L)
    val balance: StateFlow<Long> = _balance

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions

    fun addTransaction(transaction: Transaction) {
        val updatedList = _transactions.value.toMutableList()
        updatedList.add(0, transaction)
        _transactions.value = updatedList

        val newBalance = when (transaction.type) {
            TransactionType.TRANSFER -> _balance.value - transaction.nominal
            TransactionType.TOPUP -> _balance.value + transaction.nominal
        }

        _balance.value = newBalance

        Log.d("Debug", "Add Transaction: $transaction")
        Log.d("Debug", "New Balance: $newBalance")
        Log.d("Debug", "Total History: ${updatedList.size}")

        localStorage?.saveBalance(_balance.value)
        localStorage?.saveTransactions(_transactions.value)
    }
}