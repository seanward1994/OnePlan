package com.oneplan.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext

val LocalRepos = staticCompositionLocalOf<Repos> { error("Repos not provided") }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repos = Repos(this)
        setContent {
            MaterialTheme {
                CompositionLocalProvider(LocalRepos provides repos) {
                    OnePlanApp()
                }
            }
        }
    }
}
