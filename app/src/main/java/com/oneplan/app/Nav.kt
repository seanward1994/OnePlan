package com.oneplan.app
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
@Composable fun OnePlanApp() {
  OnePlanTheme {
    val nav = rememberNavController()
    Scaffold(
      topBar = { TopAppBar(title = { Text("OnePlan") }) },
      bottomBar = { OnePlanBottomBar(nav) }
    ){ pad ->
      NavHost(navController = nav, startDestination = "budget", modifier = Modifier.padding(pad)) {
        composable("budget"){ BudgetScreen() }
        composable("meals"){ MealPlanScreen() }
        composable("settings"){ SettingsScreen() }
      }
    }
  }
}
@Composable fun OnePlanBottomBar(nav: NavHostController) {
  data class Item(val route:String, val label:String, val icon:@Composable ()->Unit)
  val items = listOf(
    Item("budget","Budget",{ Icon(Icons.Filled.Home, contentDescription=null)}),
    Item("meals","Meals",{ Icon(Icons.Filled.Restaurant, contentDescription=null)}),
    Item("settings","Settings",{ Icon(Icons.Filled.Settings, contentDescription=null)}),
  )
  val current by nav.currentBackStackEntryAsState()
  val route = current?.destination?.route
  NavigationBar {
    items.forEach { itItem ->
      NavigationBarItem(
        selected = route==itItem.route,
        onClick = {
          nav.navigate(itItem.route){
            popUpTo(nav.graph.startDestinationId){ saveState=true }
            launchSingleTop=true; restoreState=true
          }
        },
        label = { Text(itItem.label) },
        icon = itItem.icon
      )
    }
  }
}
