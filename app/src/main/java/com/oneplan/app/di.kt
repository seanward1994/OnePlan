package com.oneplan.app

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("oneplan_prefs")

class Repos(context: Context) {
  private val db = Room.databaseBuilder(context, OnePlanDb::class.java, "oneplan.db")
    .fallbackToDestructiveMigration()
    .build()

  val budget = db.budget()
  val meals = db.meals()

  private val currencyKey = stringPreferencesKey("currency")
  private val caloriesKey = intPreferencesKey("daily_calories")

  val currencyFlow: Flow<String> = context.dataStore.data.map { it[currencyKey] ?: "USD" }
  val dailyCaloriesFlow: Flow<Int> = context.dataStore.data.map { it[caloriesKey] ?: 2000 }

  suspend fun setCurrency(ctx: Context, value: String) { ctx.dataStore.edit { it[currencyKey] = value } }
  suspend fun setDailyCalories(ctx: Context, value: Int) { ctx.dataStore.edit { it[caloriesKey] = value } }
}

object LocalRepos {
  // Very tiny service locator (no DI framework to keep it lean)
  @Volatile private var instance: Repos? = null
  fun get(context: Context): Repos =
    instance ?: synchronized(this) { instance ?: Repos(context.applicationContext).also { instance = it } }
}
