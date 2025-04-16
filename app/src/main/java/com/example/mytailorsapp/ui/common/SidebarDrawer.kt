package com.example.mytailorsapp.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.models.DrawerNavItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SidebarDrawer(
    drawerState: DrawerState,
    navController: NavController,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    scope: CoroutineScope,
    isDark: Boolean,
    onToggleTheme: () -> Unit,
    userId: Int = 0, // for profile route
    isAdminDashboard: Boolean = false
) {
    val navItems = if (isAdminDashboard) {
        listOf(
            DrawerNavItem(
                "Manage Workers",
                null,
                R.drawable.ic_manage_workers,
                "manage_workers_screen"
            ),
            DrawerNavItem("Manage Shops", null, R.drawable.ic_manage_shops, "manage_shops_screen"),
            DrawerNavItem(
                "Manage Inventory",
                null,
                R.drawable.ic_manage_inventory,
                "manage_inventory_screen"
            ),
            DrawerNavItem("Profile", Icons.Default.AccountCircle, null, "admin_profile_screen"),
            DrawerNavItem("Search", Icons.Default.Search, null, "worker_search_screen")
        )
    } else {
        listOf(
            DrawerNavItem("Categories", null, R.drawable.ic_category, "category_screen"),
            DrawerNavItem("Find Shops", Icons.Default.Search, null, "shop_search_screen"),
            DrawerNavItem("Inventory", Icons.AutoMirrored.Filled.List, null, "inventory_screen"),
            DrawerNavItem("Cart", Icons.Default.ShoppingCart, null, "category_screen"),
            DrawerNavItem("Profile", Icons.Default.AccountCircle, null, "profile_screen/$userId")
        )
    }

    ModalDrawerSheet(modifier = Modifier.width(240.dp)) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "StitchCraft ${if (isAdminDashboard) "Admin" else "Pro"}",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
        HorizontalDivider()

        navItems.forEachIndexed { index, item ->
            NavigationDrawerItem(
                icon = {
                    item.drawableResId?.let {
                        Icon(painter = painterResource(id = it), contentDescription = item.title)
                    } ?: item.icon?.let {
                        Icon(imageVector = it, contentDescription = item.title)
                    }
                },
                label = { Text(item.title) },
                selected = index == selectedIndex,
                onClick = {
                    onSelect(index)
                    navController.navigate(item.route.toString())
                    scope.launch { drawerState.close() }
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ✅ Theme Toggle
        NavigationDrawerItem(
            icon = {
                Text(if (isDark) "🌞" else "🌙", style = MaterialTheme.typography.titleLarge)
            },
            label = { Text(if (isDark) "Light Mode" else "Dark Mode") },
            selected = false,
            onClick = { onToggleTheme() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}
