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
                    topBar = {
                        TopAppBar(title = { Text("OnePlan") })
                    }
                ) { inner ->
                    Column(
                        modifier = Modifier
                            .padding(inner)
                            .fillMaxSize()
                    ) {
                        TabRow(selectedTabIndex = tab) {
                            Tab(selected = tab == 0, onClick = { tab = 0 }, text = { Text("Budget") })
                            Tab(selected = tab == 1, onClick = { tab = 1 }, text = { Text("Meals") })
                        }
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            when (tab) {
                                0 -> Text("Budget — coming next")
                                1 -> Text("Meal Planner — coming next")
                            }
                        }
                    }
                }
            }
        }
    }
}
