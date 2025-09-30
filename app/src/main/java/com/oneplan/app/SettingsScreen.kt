package com.oneplan.app

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen() {
    val repos = LocalRepos.current
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    val currency by repos.currencyFlow.collectAsState(initial = "USD")
    val daily by repos.dailyCaloriesFlow.collectAsState(initial = 2000)

    var currencyInput by remember(currency) { mutableStateOf(currency) }
    var caloriesInput by remember(daily) { mutableStateOf(daily.toString()) }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(value = currencyInput, onValueChange = { currencyInput = it }, label = { Text("Currency (e.g. USD, EUR)") })
        OutlinedTextField(value = caloriesInput, onValueChange = { caloriesInput = it }, label = { Text("Daily Calories Target") })

        Button(onClick = {
            scope.launch {
                repos.setCurrency(ctx, currencyInput.ifBlank { "USD" })
                repos.setDailyCalories(ctx, caloriesInput.toIntOrNull() ?: 2000)
                Toast.makeText(ctx, "Saved", Toast.LENGTH_SHORT).show()
            }
        }) { Text("Save") }

        Text("Current: $currency • $daily kcal")
    }
}
