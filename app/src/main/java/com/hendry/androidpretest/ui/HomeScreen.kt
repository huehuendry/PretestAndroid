package com.hendry.androidpretest.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hendry.androidpretest.R
import com.hendry.androidpretest.data.TransactionRepository
import com.hendry.androidpretest.model.Transaction
import com.hendry.androidpretest.model.TransactionType
import com.hendry.androidpretest.utils.formatRupiah

@Composable
fun HomeScreen(
    onNavigateToTransfer: (TransactionType) -> Unit
) {
    val transactions by TransactionRepository.transactions.collectAsState()
    val balance by TransactionRepository.balance.collectAsState()

    Column(
        modifier = Modifier
            .padding(
                top = 48.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

        Text(
            text = "Rekap keuanganmu",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Text(
            text = formatRupiah(balance),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }

        Spacer(modifier = Modifier.height(12.dp))

        SummaryCard(transactions)

        Spacer(modifier = Modifier.height(16.dp))

        ActionButtons(onNavigateToTransfer)
    }
}

@Composable
fun SummaryCard(transactions: List<Transaction>) {

    var selectedFilter by remember { mutableStateOf("EXPENSE") }

    val pemasukan = transactions
        .filter { it.type == TransactionType.TOPUP && it.status == "SUCCESS" }
        .sumOf { it.nominal }

    val pengeluaran = transactions
        .filter { it.type == TransactionType.TRANSFER && it.status == "SUCCESS" }
        .sumOf { it.nominal }

    val selisih = pemasukan - pengeluaran

    val filteredTransactions = when (selectedFilter) {
        "INCOME" -> transactions.filter {
            it.type == TransactionType.TOPUP && it.status == "SUCCESS"
        }
        "EXPENSE" -> transactions.filter {
            it.type == TransactionType.TRANSFER && it.status == "SUCCESS"
        }
        else -> transactions
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Pemasukan", style = MaterialTheme.typography.bodySmall)
                    Text(formatRupiah(pemasukan), textAlign = TextAlign.Center)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Pengeluaran", style = MaterialTheme.typography.bodySmall)
                    Text(formatRupiah(pengeluaran), textAlign = TextAlign.Center)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Selisih ${if (selisih >= 0) "+" else "-"}${formatRupiah(kotlin.math.abs(selisih))}",
                color = if (selisih >= 0) Color(0xFF00C853) else Color.Red,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                FilterButton(
                    text = "Pemasukan",
                    isSelected = selectedFilter == "INCOME",
                    onClick = { selectedFilter = "INCOME" }
                )

                FilterButton(
                    text = "Pengeluaran",
                    isSelected = selectedFilter == "EXPENSE",
                    onClick = { selectedFilter = "EXPENSE" }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(12.dp))

            filteredTransactions.take(3).forEach { item ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = if (item.type == TransactionType.TRANSFER)
                                        Color(0xFFFFEBEE) // merah soft
                                    else
                                        Color(0xFFE8F5E9), // hijau soft
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (item.type == TransactionType.TRANSFER)
                                        R.drawable.ic_money_out
                                    else
                                        R.drawable.ic_money_in
                                ),
                                contentDescription = null,
                                tint = if (item.type == TransactionType.TRANSFER)
                                    Color.Red
                                else
                                    Color(0xFF00C853),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = if (item.type == TransactionType.TRANSFER)
                                    "Uang Keluar"
                                else "Uang Masuk",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Text(
                                text = formatRupiah(item.nominal),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }

                    Icon(
                        painter = painterResource(R.drawable.ic_right),
                        contentDescription = "Detail",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RowScope.FilterButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.weight(1f),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFFDEEF5A) else Color.LightGray,
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = text,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun ActionButtons(
    onNavigateToTransfer: (TransactionType) -> Unit
) {
    Column {

        Button(
            onClick = { onNavigateToTransfer(TransactionType.TRANSFER) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF71DBD3),
                contentColor = Color.Black
            )
        ) {
            Text("Transfer Saldo")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { onNavigateToTransfer(TransactionType.TOPUP) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text("Topup Saldo")
        }
    }
}