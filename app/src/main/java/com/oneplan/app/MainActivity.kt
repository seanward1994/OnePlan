package com.oneplan.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme { OnePlanHome() }
        }
    }
}

@Composable
fun OnePlanHome() {
    var tab by remember { mutableStateOf(0) }
    Scaffold(
        topBar = { TopAppBar(title = { Text("OnePlan") }) },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = tab == 0,
                    onClick = { tab = 0 },
                    icon = { Icon(Icons.Default.Payments, contentDescription = null) },
                    label = { Text("Budget") }
                )
                NavigationBarItem(
                    selected = tab == 1,
                    onClick = { tab = 1 },
                    icon = { Icon(Icons.Default.Fastfood, contentDescription = null) },
                    label = { Text("Meals") }
                )
            }
        }
    ) { pad ->
        Box(Modifier.fillMaxSize().padding(pad), contentAlignment = Alignment.Center) {
            if (tab == 0) BudgetScreen() else MealsScreen()
        }
    }
}

@Composable
fun BudgetScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Budget — Alpha", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text("Add incomes & expenses (MVP stub).")
    }
}

@Composable
fun MealsScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Meal Planner — Alpha", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text("Plan meals & generate a list (MVP stub).")
    }
}
