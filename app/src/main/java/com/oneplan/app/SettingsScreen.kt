package com.oneplan.app

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(vm: SettingsViewModel = koinViewModel()) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    var currency by remember { mutableStateOf("USD") }
    var daily by remember { mutableStateOf("2000") }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(currency, { currency = it }, label = { Text("Currency (e.g. USD)") })
        OutlinedTextField(daily, { daily = it }, label = { Text("Daily Calories") })

        Button(onClick = {
            scope.launch {
                vm.save(currency.ifBlank { "USD" }, daily.toIntOrNull() ?: 2000)
                Toast.makeText(ctx, "Saved", Toast.LENGTH_SHORT).show()
            }
        }) { Text("Save") }
    }
}
