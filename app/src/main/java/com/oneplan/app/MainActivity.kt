package com.oneplan.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnePlanTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    var clicks by remember { mutableStateOf(0) }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "OnePlan Alpha")
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { clicks++ }) {
                            Text("Tap to test • $clicks")
                        }
                    }
                }
            }
        }
    }
}
