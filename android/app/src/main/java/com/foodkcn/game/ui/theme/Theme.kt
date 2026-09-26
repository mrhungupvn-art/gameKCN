package com.foodkcn.game.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = KcnOrange,
    onPrimary = Color(0xFFFFFFFF),
    secondary = KcnBlue,
    background = KcnBackground,
    surface = KcnSurface
)

@Composable
fun KCNGameTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography,
        content = content
    )
}
