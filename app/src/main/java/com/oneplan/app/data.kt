package com.oneplan.app

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.*

val Context.dataStore by preferencesDataStore("oneplan_prefs")

@Entity(tableName = "budget_items")
data class BudgetItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val label: String,
    val amountCents: Long,
    val category: String = "General",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "meals")
data class Meal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val calories: Int = 0,
    val day: String = ""
)

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budget_items ORDER BY timestamp DESC")
    suspend fun list(): List<BudgetItem>
    @Insert suspend fun add(item: BudgetItem)
    @Delete suspend fun remove(item: BudgetItem)
}

@Dao
interface MealDao {
    @Query("SELECT * FROM meals ORDER BY id DESC")
    suspend fun list(): List<Meal>
    @Insert suspend fun add(meal: Meal)
    @Delete suspend fun remove(meal: Meal)
}

@Database(entities = [BudgetItem::class, Meal::class], version = 1)
abstract class OnePlanDb : RoomDatabase() {
    abstract fun budget(): BudgetDao
    abstract fun meals(): MealDao
}

class Repos(private val context: Context) {
    private val currencyKey = stringPreferencesKey("currency")
    private val caloriesKey = intPreferencesKey("daily_calories")

    suspend fun setCurrency(value: String) {
        context.dataStore.edit { it[currencyKey] = value }
    }
    suspend fun setDailyCalories(value: Int) {
        context.dataStore.edit { it[caloriesKey] = value }
    }
}
