package com.example.testekotlin.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.res.colorResource
import com.example.testekotlin.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun TesteKotlinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val darkColorScheme = darkColorScheme(
        primary = colorResource(R.color.md_theme_onPrimaryContainer),
        secondary = colorResource(R.color.md_theme_onSecondaryContainer),
        tertiary = colorResource(R.color.md_theme_onPrimaryContainer),
    )

    val lightColorScheme = lightColorScheme(
        primary = colorResource(R.color.md_theme_primary),
        secondary = colorResource(R.color.md_theme_secondary),
        tertiary = colorResource(R.color.md_theme_tertiary)
    )

    val colorScheme = when {
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    val systemUiCOntroller = rememberSystemUiController()

    SideEffect {
        systemUiCOntroller.setStatusBarColor(
            color = colorScheme.primary,
            darkIcons = !darkTheme
        )

        systemUiCOntroller.setNavigationBarColor(
            color = colorScheme.background
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}