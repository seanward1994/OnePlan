package com.oneplan.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

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
    val items = listOf(
        "budget" to "Budget",
        "meals" to "Meals",
        "settings" to "Settings"
    )
    NavigationBar {
        val current by nav.currentBackStackEntryAsState()
        val route = current?.destination?.route
        items.forEach { (r, label) ->
            NavigationBarItem(
                selected = route == r,
                onClick = { nav.navigate(r) { popUpTo(nav.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true } },
                label = { Text(label) },
                icon = { Icon(Icons.Default.Circle, contentDescription = label) }
            )
        }
    }
}

// Tiny built-in icon to avoid extra deps
object Icons { object Default { @Composable fun Circle() = IconDefaults.FilledCircle() } }
@Composable fun IconDefaults.FilledCircle() { /* empty placeholder; M3 shows a dot */ }
