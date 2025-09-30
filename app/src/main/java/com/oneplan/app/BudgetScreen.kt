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
fun BudgetScreen(vm: BudgetViewModel = koinViewModel()) {
    val scope = rememberCoroutineScope()
    val items by vm.items.collectAsState()
    var showAdd by remember { mutableStateOf(false) }

    Scaffold(floatingActionButton = { FloatingActionButton({ showAdd = true }) { Text("+") } }) { pad ->
        Column(Modifier.fillMaxSize().padding(pad).padding(16.dp)) {
            Text("Budget", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(12.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(items) { item ->
                    Card {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(item.label, style = MaterialTheme.typography.titleMedium)
                                Text("$" + "%.2f".format(item.amountCents / 100.0))
                            }
                            OutlinedButton(onClick = { scope.launch { vm.remove(item) } }) { Text("Delete") }
                        }
                    }
                }
            }
        }
    }

    if (showAdd) {
        AddBudgetDialog(
            onDismiss = { showAdd = false },
            onAdd = { label, cents -> scope.launch { vm.add(label, cents); showAdd = false } }
        )
    }
}

@Composable
private fun AddBudgetDialog(onDismiss: () -> Unit, onAdd: (String, Long) -> Unit) {
    var label by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Budget Item") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = label, onValueChange = { label = it }, label = { Text("Label") })
                OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount (12.34)") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val cents = ((amount.toDoubleOrNull() ?: 0.0) * 100).toLong()
                onAdd(label.trim(), cents)
            }) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
