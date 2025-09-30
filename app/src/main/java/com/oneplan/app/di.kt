package com.oneplan.app

import android.content.Context
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("oneplan_prefs")

class Repos(context: Context) {
    private val db = Room.databaseBuilder(
        context, OnePlanDb::class.java, "oneplan.db"
    ).fallbackToDestructiveMigration().build()

    val budget = db.budget()
    val meals = db.meals()

    // Simple settings
    private val currencyKey = stringPreferencesKey("currency")
    private val caloriesKey = intPreferencesKey("daily_calories")

    val currencyFlow: Flow<String> = context.dataStore.data.map { it[currencyKey] ?: "USD" }
    val dailyCaloriesFlow: Flow<Int> = context.dataStore.data.map { it[caloriesKey] ?: 2000 }

    suspend fun setCurrency(context: Context, value: String) {
        context.dataStore.edit { it[currencyKey] = value }
    }
    suspend fun setDailyCalories(context: Context, value: Int) {
        context.dataStore.edit { it[caloriesKey] = value }
    }
}

val LocalRepos = staticCompositionLocalOf<Repos> {
    error("LocalRepos not provided")
}
