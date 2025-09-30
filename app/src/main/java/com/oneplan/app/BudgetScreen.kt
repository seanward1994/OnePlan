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
@Composable fun BudgetScreen() {
    val repos = LocalRepos.current
    val scope = rememberCoroutineScope()
    var items by remember { mutableStateOf(listOf<BudgetItem>()) }
    var showAdd by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { items = repos.budget.list() }
    fun reload() = scope.launch { items = repos.budget.list() }
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
                                Text(item.category, style = MaterialTheme.typography.bodySmall)
                            }
                            Text("$" + "%.2f".format(item.amountCents / 100.0))
                            Spacer(Modifier.width(8.dp))
                            OutlinedButton(onClick = { scope.launch { repos.budget.remove(item); reload() } }) { Text("Delete") }
                        }
                    }
                }
            }
        }
    }
    if (showAdd) AddBudgetDialog(onDismiss = { showAdd = false }) { label, cents, cat ->
        val cleaned = label.trim()
        if (cleaned.isNotEmpty() && cents > 0) {
            scope.launch { repos.budget.add(BudgetItem(label = cleaned, amountCents = cents, category = cat)); reload() }
        }
        showAdd = false
    }
}
@Composable private fun AddBudgetDialog(onDismiss: () -> Unit, onAdd: (String, Long, String) -> Unit) {
    var label by remember { mutableStateOf(TextFieldValue("")) }
    var amount by remember { mutableStateOf(TextFieldValue("")) }
    var category by remember { mutableStateOf(TextFieldValue("General")) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Budget Item") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = label, onValueChange = { label = it }, label = { Text("Label") })
                OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount (12.34)") })
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val cents = ((amount.text.toDoubleOrNull() ?: 0.0) * 100).toLong()
                onAdd(label.text, cents, category.text.ifBlank { "General" })
            }) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
