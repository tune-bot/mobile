package net.tune_bot.view

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val darkColors = Colors(
    primary = Color( 0xFF000000),
    primaryVariant = Color(0xFF000000),
    onPrimary = Color(0xFFCC3921),
    secondary = Color(0xFF000000),
    secondaryVariant = Color(0xFF000000),
    onSecondary = Color(0xFFCC3921),
    background = Color(0xFF232323),
    onBackground = Color(0xFFF0F0F0),
    surface = Color(0xFF000000),
    onSurface = Color(0xFFCC3921),
    error = Color(0xFFFF8888),
    onError = Color(0xFF000000),
    isLight = true
)

private val lightColors = Colors(
    primary = Color( 0xFF114122),
    primaryVariant = Color(0xFF114122),
    onPrimary = Color(0xFF2139BB),
    secondary = Color(0xFF114122),
    secondaryVariant = Color(0xFFFF114122),
    onSecondary = Color(0xFF2139BB),
    background = Color(0xFFF00001),
    onBackground = Color(0xFF000000),
    surface = Color(0xFFA1A1A1),
    onSurface = Color(0xFF000000),
    error = Color(0xFFFF8888),
    onError = Color(0xFF000000),
    isLight = false
)

@Composable
fun Theme(
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) = MaterialTheme(
    if(isDark) darkColors else lightColors,
    typography = Typography(),
    shapes = Shapes(),
    content = content
)
