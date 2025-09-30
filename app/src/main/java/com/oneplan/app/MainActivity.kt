package com.oneplan.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MaterialTheme(colorScheme = lightColorScheme()) {
        var tab by remember { mutableStateOf(0) }
        Scaffold(
          topBar = { TopAppBar(title = { Text("OnePlan MegaAlpha") }) }
        ) { pad ->
          Column(Modifier.padding(pad).fillMaxSize()) {
            TabRow(selectedTabIndex = tab) {
              Tab(selected = tab == 0, onClick = { tab = 0 }, text = { Text("Budget") })
              Tab(selected = tab == 1, onClick = { tab = 1 }, text = { Text("Meals") })
              Tab(selected = tab == 2, onClick = { tab = 2 }, text = { Text("AI") })
            }
            when (tab) {
              0 -> Text("Budget — ready to wire", Modifier.padding(16.dp))
              1 -> Text("Meals — ready to wire", Modifier.padding(16.dp))
              2 -> Text("AI — plug provider later", Modifier.padding(16.dp))
            }
          }
        }
      }
    }
  }
}
