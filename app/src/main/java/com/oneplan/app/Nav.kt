package com.oneplan.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings

@Composable
fun OnePlanApp() {
    val nav = rememberNavController()
    Scaffold(
        topBar = { TopAppBar(title = { Text("OnePlan") }) },
        bottomBar = { OnePlanBottomBar(nav) }
    ) { pad ->
        NavHost(navController = nav, startDestination = "budget", modifier = Modifier.padding(pad)) {
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
        Item("budget", "Budget", { Icon(Icons.Filled.Home, contentDescription = "Budget") }),
        Item("meals", "Meals", { Icon(Icons.Filled.Restaurant, contentDescription = "Meals") }),
        Item("settings", "Settings", { Icon(Icons.Filled.Settings, contentDescription = "Settings") }),
    )
    NavigationBar {
        val current by nav.currentBackStackEntryAsState()
        val route = current?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                selected = route == item.route,
                onClick = {
                    nav.navigate(item.route) {
                        popUpTo(nav.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = { Text(item.label) },
                icon = item.icon
            )
        }
    }
}
