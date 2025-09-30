package com.oneplan.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnePlanTheme {
                var tab by remember { mutableStateOf(0) }
                Scaffold(
                    topBar = { TopAppBar(title = { Text("OnePlan Alpha") }) }
                ) { inner ->
                    Column(Modifier.padding(inner).fillMaxSize()) {
                        TabRow(selectedTabIndex = tab) {
                            Tab(selected = tab == 0, onClick = { tab = 0 }, text = { Text("Home") })
                            Tab(selected = tab == 1, onClick = { tab = 1 }, text = { Text("About") })
                        }
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            when (tab) {
                                0 -> Text("Hello 👋 — Alpha is running.")
                                1 -> Text("Material3 theme fixed. CI should be green.")
                            }
                        }
                    }
                }
            }
        }
    }
}
