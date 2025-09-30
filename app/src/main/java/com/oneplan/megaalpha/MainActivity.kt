package com.oneplan.megaalpha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import androidx.room.*
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

// DataStore helper
val Context.dataStore by preferencesDataStore(name = "oneplan_prefs")

@Entity(tableName = "samples")
data class Sample(@PrimaryKey(autoGenerate = true) val id: Long = 0, val title: String = "", val ts: Long = System.currentTimeMillis())

@Dao
interface SampleDao {
    @Query("SELECT * FROM samples ORDER BY id DESC")
    suspend fun list(): List<Sample>
    @Insert
    suspend fun add(s: Sample)
    @Delete
    suspend fun remove(s: Sample)
}

@Database(entities = [Sample::class], version = 1)
abstract class AppDb: RoomDatabase() { abstract fun sampleDao(): SampleDao }

class Repos(context: android.content.Context) {
    private val db = Room.databaseBuilder(context, AppDb::class.java, "oneplan.db").fallbackToDestructiveMigration().build()
    val sampleDao = db.sampleDao()
    private val ctx = context
    private val key = stringPreferencesKey("currency")
    suspend fun getCurrency(): String = ctx.dataStore.data.map { it[key] ?: "USD" }.first()
    suspend fun setCurrency(v: String) { ctx.dataStore.edit { it[key] = v } }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repos = Repos(this)
        setContent {
            MaterialTheme {
                val scope = rememberCoroutineScope()
                var list by remember { mutableStateOf(listOf<Sample>()) }
                LaunchedEffect(Unit) { list = repos.sampleDao.list() }
                Scaffold(
                    topBar = { TopAppBar(title = { Text("OnePlanMegaAlpha") }) },
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            scope.launch {
                                repos.sampleDao.add(Sample(title = "Item ${System.currentTimeMillis()}"))
                                list = repos.sampleDao.list()
                            }
                        }) { Text("+") }
                    }
                ) { pad ->
                    Column(Modifier.fillMaxSize().padding(pad).padding(16.dp)) {
                        Text("Samples", style = MaterialTheme.typography.headlineSmall)
                        Spacer(Modifier.height(12.dp))
                        for (it in list) {
                            Card(Modifier.fillMaxWidth().padding(4.dp)) {
                                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Column(Modifier.weight(1f)) { Text(it.title); Text("${it.ts}", style = MaterialTheme.typography.bodySmall) }
                                    TextButton(onClick = {
                                        scope.launch { repos.sampleDao.remove(it); list = repos.sampleDao.list() }
                                    }) { Text("Delete") }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
