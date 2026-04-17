package com.hendry.androidpretest.data
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hendry.androidpretest.model.Transaction

class LocalStorage(context: Context) {

    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveBalance(balance: Long) {
        prefs.edit().putLong("balance", balance).apply()
    }

    fun getBalance(): Long {
        return prefs.getLong("balance", 500_000L)
    }

    fun saveTransactions(list: List<Transaction>) {
        val json = gson.toJson(list)
        prefs.edit().putString("transactions", json).apply()
    }

    fun getTransactions(): List<Transaction> {
        val json = prefs.getString("transactions", null) ?: return emptyList()
        val type = object : TypeToken<List<Transaction>>() {}.type
        return gson.fromJson(json, type)
    }
}