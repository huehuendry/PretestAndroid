package com.hendry.androidpretest.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hendry.androidpretest.model.Transaction
import com.hendry.androidpretest.model.TransactionType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.hendry.androidpretest.data.TransactionRepository
import com.hendry.androidpretest.network.ExecutionRequest
import com.hendry.androidpretest.network.RetrofitClient

class GatherViewModel : ViewModel() {

    private val _state = MutableStateFlow(GatherState())
    val state: StateFlow<GatherState> = _state
    val balance = TransactionRepository.balance

    private val _event = MutableSharedFlow<GatherEvent>()
    val event: SharedFlow<GatherEvent> = _event

    fun setType(type: TransactionType) {
        _state.value = _state.value.copy(type = type)
    }

    fun onAmountChanged(input: String) {
        val amountLong = input.toLongOrNull()

        val error = when {
            input.isEmpty() -> "Nominal harus diisi"
            amountLong == null -> "Nominal tidak valid"
            amountLong < 10_000 -> "Minimal Rp10.000"
            amountLong > 1_000_000_000 -> "Maksimal Rp1.000.000.000"
            _state.value.type == TransactionType.TRANSFER &&
                    amountLong > balance.value -> "Saldo tidak mencukupi"
            else -> null
        }

        _state.value = _state.value.copy(
            amount = input,
            error = error,
            isButtonEnabled = error == null && input.isNotEmpty()
        )
    }

    fun onClearClicked() {
        _state.value = _state.value.copy(
            amount = "",
            error = null,
            isButtonEnabled = false
        )
    }

    fun onSubmit() {
        val currentState = _state.value
        val amountLong = currentState.amount.toLongOrNull() ?: return

        viewModelScope.launch {
            _state.value = currentState.copy(isLoading = true)

            try {
                val request = ExecutionRequest(
                    reffId = generateReffId(),
                    time = getCurrentTime(),
                    nominal = amountLong,
                    type = currentState.type.name
                )

                val response = RetrofitClient.apiService.executeTransaction(request)

                val status = when (response.code()) {
                    200 -> "SUCCESS"
                    409 -> "FAILED"
                    else -> "FAILED"
                }

                Log.d("Status API", "Final Status: $status")

                val transaction = Transaction(
                    reffId = request.reffId,
                    time = request.time,
                    nominal = amountLong,
                    type = currentState.type,
                    status = status
                )

                if (status == "SUCCESS") {
                    TransactionRepository.addTransaction(transaction)
                }

                _event.emit(GatherEvent.NavigateToReceipt(transaction))

            } catch (e: Exception) {
                val transaction = Transaction(
                    reffId = generateReffId(),
                    time = getCurrentTime(),
                    nominal = amountLong,
                    type = currentState.type,
                    status = "FAILED"
                )

                _event.emit(GatherEvent.NavigateToReceipt(transaction))
            }

            _state.value = _state.value.copy(isLoading = false)

        }
    }

    private fun generateReffId(): String {
        return (1..13)
            .map { ('0'..'9').random() }
            .joinToString("")
    }

    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}