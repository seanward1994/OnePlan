package com.oneplan.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MealPlanScreen(vm: MealViewModel = koinViewModel()) {
    val scope = rememberCoroutineScope()
    val items by vm.items.collectAsState()
    var showAdd by remember { mutableStateOf(false) }

    Scaffold(floatingActionButton = { FloatingActionButton({ showAdd = true }) { Text("+") } }) { pad ->
        Column(Modifier.fillMaxSize().padding(pad).padding(16.dp)) {
            Text("Meals", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(12.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(items) { item ->
                    Card {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(item.title, style = MaterialTheme.typography.titleMedium)
                                Text("${item.calories} kcal  ${item.day}", style = MaterialTheme.typography.bodySmall)
                            }
                            OutlinedButton(onClick = { scope.launch { vm.remove(item) } }) { Text("Delete") }
                        }
                    }
                }
            }
        }
    }

    if (showAdd) {
        AddMealDialog(
            onDismiss = { showAdd = false },
            onAdd = { title, kcal, day -> scope.launch { vm.add(title, kcal, day); showAdd = false } }
        )
    }
}

@Composable
private fun AddMealDialog(onDismiss: () -> Unit, onAdd: (String, Int, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Meal") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                OutlinedTextField(value = calories, onValueChange = { calories = it }, label = { Text("Calories") })
                OutlinedTextField(value = day, onValueChange = { day = it }, label = { Text("Day") })
            }
        },
        confirmButton = {
            TextButton(onClick = { onAdd(title.trim(), calories.toIntOrNull() ?: 0, day.trim()) }) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
