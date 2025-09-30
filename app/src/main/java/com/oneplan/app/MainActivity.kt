package com.oneplan.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.staticCompositionLocalOf

val LocalRepos = staticCompositionLocalOf<Repos> { error("Repos not provided") }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repos = Repos(this)
        setContent {
            OnePlanTheme {
                androidx.compose.runtime.CompositionLocalProvider(LocalRepos provides repos) {
                    OnePlanApp()
                }
            }
        }
    }
}
