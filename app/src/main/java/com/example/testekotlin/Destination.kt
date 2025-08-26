package com.example.testekotlin

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
    ALL("alls","Alls",Icons.Default.Home,"Alls"),
    FAVORITE("favorite","Favorite",Icons.Default.Star,"Favorite")
}