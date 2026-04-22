package com.hendry.androidpretest.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hendry.androidpretest.model.Transaction

@Composable
fun ReceiptScreen(
    transaction: Transaction,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp) .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = if (transaction.status == "SUCCESS")
                "Transfer Berhasil"
            else "Transfer Gagal"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Nominal: ${transaction.nominal}")
        Text("Waktu: ${transaction.time}")
        Text("Ref ID: ${transaction.reffId}")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack) {
            Text("Kembali")
        }
    }
}