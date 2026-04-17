package com.hendry.androidpretest.utils

import java.text.NumberFormat
import java.util.Locale

fun formatRupiah(amount: Long): String {
    val localeID = Locale("in", "ID")
    val format = NumberFormat.getCurrencyInstance(localeID)

    format.maximumFractionDigits = 0
    format.minimumFractionDigits = 0

    return format.format(amount)
}

fun formatNumberOnly(amount: Long): String {
    val format = NumberFormat.getNumberInstance(Locale("in", "ID"))
    return format.format(amount)
}