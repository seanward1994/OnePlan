import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import android.app.Application
package com.oneplan.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { val repos = remember { Repos(this) }; CompositionLocalProvider(LocalRepos provides repos) { OnePlanApp() } }
    }
}
