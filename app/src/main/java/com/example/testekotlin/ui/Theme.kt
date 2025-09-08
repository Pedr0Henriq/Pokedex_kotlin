package com.example.testekotlin.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.example.testekotlin.R


@Composable
fun TesteKotlinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val darkColorScheme = darkColorScheme(
        primary = colorResource(R.color.pokemon_red),
        secondary = colorResource(R.color.pokemon_background),
        tertiary = colorResource(R.color.md_theme_onPrimaryContainer),
    )

    val lightColorScheme = lightColorScheme(
        primary = colorResource(R.color.pokemon_red),
        secondary = colorResource(R.color.pokemon_background),
        tertiary = colorResource(R.color.md_theme_tertiary)
    )

    val colorScheme = when {
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}