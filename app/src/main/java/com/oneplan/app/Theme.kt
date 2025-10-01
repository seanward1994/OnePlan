package com.oneplan.app
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
private val Colors = lightColorScheme()
@Composable fun OnePlanTheme(content: @Composable () -> Unit) {
  MaterialTheme(colorScheme = Colors, content = content)
}
