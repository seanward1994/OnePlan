package com.oneplan.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repos = Repos(applicationContext)
        setContent {
            CompositionLocalProvider(LocalRepos provides repos) {
                OnePlanTheme {
                    OnePlanApp()
                }
            }
        }
    }
}
