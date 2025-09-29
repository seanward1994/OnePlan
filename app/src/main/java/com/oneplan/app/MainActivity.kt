package com.oneplan.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val repos = remember { Repos(applicationContext) }
            CompositionLocalProvider(LocalRepos provides repos) {
                OnePlanTheme { OnePlanApp() }
            }
        }
    }
}
