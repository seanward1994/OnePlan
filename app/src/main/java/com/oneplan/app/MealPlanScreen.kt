package com.oneplan.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun MealPlanScreen() {
    val repos = LocalRepos.current
    val scope = rememberCoroutineScope()
    var items by remember { mutableStateOf(listOf<Meal>()) }
    var showAdd by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { items = repos.meals.list() }
    fun reload() = scope.launch { items = repos.meals.list() }

    Scaffold(floatingActionButton = { FloatingActionButton(onClick = { showAdd = true }) { Text("+") } }) { pad ->
        Column(Modifier.fillMaxSize().padding(pad).padding(16.dp)) {
            Text("Meals", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(12.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(items) { item ->
                    Card {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(item.title, style = MaterialTheme.typography.titleMedium)
                                Text("${item.calories} kcal  ${item.day}", style = MaterialTheme.typTypography.bodySmall)
                            }
                            OutlinedButton(onClick = { scope.launch { repos.meals.remove(item); reload() } }) { Text("Delete") }
                        }
                    }
                }
            }
        }
    }

    if (showAdd) AddMealDialog(
        onDismiss = { showAdd = false },
        onAdd = { title, kcal, day -> scope.launch { repos.meals.add(Meal(title = title, calories = kcal, day = day)); reload(); showAdd = false } }
    )
}

@Composable
private fun AddMealDialog(onDismiss: () -> Unit, onAdd: (String, Int, String) -> Unit) {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var calories by remember { mutableStateOf(TextFieldValue("")) }
    var day by remember { mutableStateOf(TextFieldValue("")) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Meal") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(label = { Text("Title") }, value = title, onValueChange = { title = it })
                OutlinedTextField(label = { Text("Calories") }, value = calories, onValueChange = { calories = it })
                OutlinedTextField(label = { Text("Day") }, value = day, onValueChange = { day = it })
            }
        },
        confirmButton = {
            TextButton(onClick = { onAdd(title.text.trim(), calories.text.toIntOrNull() ?: 0, day.text.trim()) }) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
