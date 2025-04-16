package com.example.mytailorsapp.data.models

import androidx.compose.ui.graphics.vector.ImageVector

data class DrawerNavItem(
    val title: String,
    val icon: ImageVector? = null,
    val drawableResId: Int? = null,
    val route: String? = null,  // Optional if it's just an action item
    val onClick: (() -> Unit)? = null  // Optional custom action
)