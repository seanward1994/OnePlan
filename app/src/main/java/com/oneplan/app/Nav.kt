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
  OnePlanTheme {
    Scaffold(
      topBar = { TopAppBar(title = { Text("OnePlan") }) },
      bottomBar = { BottomBar(nav) }
    ) { pad ->
      NavHost(navController = nav, startDestination = "budget", modifier = Modifier.padding(pad)) {
        composable("budget") { BudgetScreen() }
        composable("meals") { MealPlanScreen() }
        composable("settings") { SettingsScreen() }
      }
    }
  }
}

@Composable
private fun BottomBar(nav: NavHostController) {
  data class Item(val route: String, val label: String, val icon: @Composable () -> Unit)
  val items = listOf(
    Item("budget", "Budget", { Icon(Icons.Filled.Home, contentDescription = "Budget") }),
    Item("meals", "Meals", { Icon(Icons.Filled.Restaurant, contentDescription = "Meals") }),
    Item("settings", "Settings", { Icon(Icons.Filled.Settings, contentDescription = "Settings") })
  )
  NavigationBar {
    val backStack by nav.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route
    items.forEach { itx ->
      NavigationBarItem(
        selected = currentRoute == itx.route,
        onClick = {
          nav.navigate(itx.route) {
            popUpTo(nav.graph.startDestinationId) { saveState = true }
            launchSingleTop = true
            restoreState = true
          }
        },
        label = { Text(itx.label) },
        icon = itx.icon
      )
    }
  }
}
