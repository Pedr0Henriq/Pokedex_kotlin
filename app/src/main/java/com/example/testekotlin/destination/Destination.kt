package com.example.testekotlin.destination

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

enum class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    ALL("alls","Todos", Icons.Default.Home,"Todos"),
    FAVORITE("favorite","Favoritos", Icons.Default.Star,"Favoritos")
}