package com.oneplan.app

import androidx.room.*

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
