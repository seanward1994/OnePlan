package com.oneplan.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BudgetViewModel(private val dao: BudgetDao) : ViewModel() {
    private val _items = MutableStateFlow<List<BudgetItem>>(emptyList())
    val items = _items.asStateFlow()

    init { refresh() }

    private fun refresh() = viewModelScope.launch { _items.value = dao.list() }
    suspend fun add(label: String, cents: Long) { dao.add(BudgetItem(label = label, amountCents = cents)); refresh() }
    suspend fun remove(item: BudgetItem) { dao.remove(item); refresh() }
}

class MealViewModel(private val dao: MealDao) : ViewModel() {
    private val _items = MutableStateFlow<List<Meal>>(emptyList())
    val items = _items.asStateFlow()

    init { refresh() }

    private fun refresh() = viewModelScope.launch { _items.value = dao.list() }
    suspend fun add(title: String, kcal: Int, day: String) { dao.add(Meal(title = title, calories = kcal, day = day)); refresh() }
    suspend fun remove(meal: Meal) { dao.remove(meal); refresh() }
}

class SettingsViewModel(private val repos: Repos) : ViewModel() {
    suspend fun save(currency: String, daily: Int) {
        repos.setCurrency(currency)
        repos.setDailyCalories(daily)
    }
}
