package com.oneplan.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

@Composable
fun OnePlanApp() {
    val nav = rememberNavController()
    Scaffold(
        topBar = { TopAppBar(title = { Text("OnePlanAlpha") }) },
        bottomBar = { OnePlanBottomBar(nav) }
    ) { pad ->
        NavHost(nav, startDestination = "budget", Modifier.padding(pad)) {
            composable("budget") { BudgetScreen() }
            composable("meals") { MealPlanScreen() }
            composable("settings") { SettingsScreen() }
        }
    }
}

@Composable
fun OnePlanBottomBar(nav: NavHostController) {
    data class Item(val route: String, val label: String, val icon: @Composable () -> Unit)
    val items = listOf(
        Item("budget", "Budget") { Icon(Icons.Filled.Home, null) },
        Item("meals", "Meals") { Icon(Icons.Filled.Restaurant, null) },
        Item("settings", "Settings") { Icon(Icons.Filled.Settings, null) },
    )
    NavigationBar {
        val current by nav.currentBackStackEntryAsState()
        val route = current?.destination?.route
        items.forEach { i ->
            NavigationBarItem(
                selected = route == i.route,
                onClick = { nav.navigate(i.route) { popUpTo(nav.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true } },
                label = { Text(i.label) },
                icon = i.icon
            )
        }
    }
}
