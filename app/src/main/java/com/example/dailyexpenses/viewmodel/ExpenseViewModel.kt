package com.example.dailyexpenses.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyexpenses.model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ExpenseViewModel : ViewModel() {
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    val total: StateFlow<Double> = expenses
        .map { items -> items.sumOf(Expense::amount) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    private val _showDialogFor = MutableStateFlow<Int?>(null)
    val showDialogFor: StateFlow<Int?> = _showDialogFor.asStateFlow()

    private var nextId = 0

    fun addExpense(title: String, amount: Double) {
        if (title.isBlank() || !amount.isFinite() || amount <= 0) return
        _expenses.value = _expenses.value + Expense(nextId++, title.trim(), amount)
    }

    fun requestDelete(id: Int) { _showDialogFor.value = id }
    fun cancelDelete() { _showDialogFor.value = null }

    fun confirmDelete() {
        _showDialogFor.value?.let { id ->
            _expenses.value = _expenses.value.filterNot { it.id == id }
        }
        _showDialogFor.value = null
    }
}
