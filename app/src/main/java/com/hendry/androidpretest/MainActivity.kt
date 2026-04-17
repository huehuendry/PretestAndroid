package com.hendry.androidpretest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import com.hendry.androidpretest.ui.AppScreen
import com.hendry.androidpretest.data.TransactionRepository

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TransactionRepository.init(applicationContext)

        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                Surface(
                    color = colorScheme.background
                ) {
                    AppScreen()
                }
            }
        }
    }
}