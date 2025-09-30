package com.oneplan.app

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class OnePlanAppClass : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                appModule(this@OnePlanAppClass),
                module {
                    viewModel { BudgetViewModel(get()) }
                    viewModel { MealViewModel(get()) }
                    viewModel { SettingsViewModel(get()) }
                }
            )
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { OnePlanTheme { OnePlanApp() } }
    }
}
