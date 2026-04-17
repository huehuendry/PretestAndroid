package com.hendry.androidpretest.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hendry.androidpretest.model.Transaction
import com.hendry.androidpretest.model.TransactionType
import com.hendry.androidpretest.utils.formatNumberOnly
import com.hendry.androidpretest.utils.formatRupiah
import com.hendry.androidpretest.viewmodel.GatherEvent
import com.hendry.androidpretest.viewmodel.GatherViewModel

@Composable
fun GatherScreen(
    type: TransactionType,
    viewModel: GatherViewModel = viewModel(),
    onNavigateToReceipt: (Transaction) -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val balance by viewModel.balance.collectAsState()

    LaunchedEffect(type) {
        viewModel.setType(type)
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is GatherEvent.NavigateToReceipt -> {
                    onNavigateToReceipt(event.transaction)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            Text(
                text = if (state.type == TransactionType.TRANSFER)
                    "Transfer Saldo"
                else "Topup Saldo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Nominal")

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = if (state.amount.isEmpty()) ""
            else formatNumberOnly(state.amount.toLong()),

            onValueChange = { input ->
                val cleanInput = input.replace("[^0-9]".toRegex(), "")
                viewModel.onAmountChanged(cleanInput)
            },

            isError = state.error != null,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.headlineSmall,

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),

            prefix = {
                Text("Rp ")
            },

            trailingIcon = {
                if (state.amount.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onClearClicked() }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            }
        )

        // 🔹 ERROR
        state.error?.let {
            Text(
                text = it,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.type == TransactionType.TRANSFER) {

            Text("Sumber dana")

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F5F5)
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text("Total Saldo", style = MaterialTheme.typography.bodySmall)
                    Text(
                        formatRupiah(balance),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { viewModel.onSubmit() },
            enabled = state.isButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                if (state.type == TransactionType.TRANSFER)
                    "Transfer"
                else "Topup"
            )
        }

        if (state.isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}