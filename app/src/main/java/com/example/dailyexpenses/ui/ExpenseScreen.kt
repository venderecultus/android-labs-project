package com.example.dailyexpenses.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.dailyexpenses.viewmodel.ExpenseViewModel
import java.util.Locale

@Composable
fun ExpenseScreen(viewModel: ExpenseViewModel) {
    val expenses by viewModel.expenses.collectAsState()
    val total by viewModel.total.collectAsState()
    val dialogId by viewModel.showDialogFor.collectAsState()
    var title by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    val amount = amountText.replace(',', '.').toDoubleOrNull()

    Scaffold { insets ->
        Column(
            modifier = Modifier.fillMaxSize().padding(insets).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Витрати за день", style = MaterialTheme.typography.headlineMedium)
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Загальна сума", style = MaterialTheme.typography.titleMedium)
                    Text("${String.format(Locale.getDefault(), "%.2f", total)} ₴", style = MaterialTheme.typography.headlineSmall)
                }
            }
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Назва витрати") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            OutlinedTextField(
                value = amountText,
                onValueChange = { value -> if (value.all { it.isDigit() || it == '.' || it == ',' }) amountText = value },
                label = { Text("Сума") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Button(
                onClick = {
                    viewModel.addExpense(title, amount ?: 0.0)
                    title = ""
                    amountText = ""
                },
                enabled = title.isNotBlank() && amount != null && amount > 0 && amount.isFinite(),
                modifier = Modifier.fillMaxWidth(),
            ) { Text("Додати") }
            Text("Список витрат", style = MaterialTheme.typography.titleLarge)
            if (expenses.isEmpty()) {
                Text(
                    "Поки що витрат немає",
                    modifier = Modifier.weight(1f).padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(expenses, key = { it.id }) { expense ->
                        ExpenseCard(expense) { viewModel.requestDelete(expense.id) }
                    }
                }
            }
        }
    }

    if (dialogId != null) {
        AlertDialog(
            onDismissRequest = viewModel::cancelDelete,
            title = { Text("Видалити витрату?") },
            text = { Text("Цю дію неможливо скасувати.") },
            confirmButton = { TextButton(onClick = viewModel::confirmDelete) { Text("Видалити") } },
            dismissButton = { TextButton(onClick = viewModel::cancelDelete) { Text("Скасувати") } },
        )
    }
}
