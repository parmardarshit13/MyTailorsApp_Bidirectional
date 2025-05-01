package com.example.mytailorsapp.ui.common

import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.mytailorsapp.R
import com.google.android.material.navigation.NavigationView
import androidx.core.view.size

object SidebarMenuHelper {

    // IDs and Titles for your menu items
    enum class Option(val id: Int, val title: String, val iconRes: Int) {
        // Admin Options
        ADMIN_DASHBOARD(R.id.nav_admin_dashboard, "Dashboard", R.drawable.ic_home),
        ADMIN_WORKERS(R.id.nav_manage_workers, "Manage Workers", R.drawable.ic_manage_workers),
//        ADMIN_SHOPS(R.id.nav_manage_shops, "Manage Shops", R.drawable.ic_manage_shops),
        ADMIN_INVENTORY(R.id.nav_manage_inventory, "Manage Inventory", R.drawable.ic_manage_inventory),
        ADMIN_SEARCH(R.id.nav_search, "Search Workers", R.drawable.ic_search),
        ADMIN_PROFILE(R.id.nav_profile, "Profile", R.drawable.ic_profile),

        // Customer Options
        CUSTOMER_DASHBOARD(R.id.nav_customer_dashboard, "Dashboard", R.drawable.ic_home),
        CUSTOMER_CATEGORIES(R.id.nav_customer_categories, "Categories", R.drawable.ic_category),
        CUSTOMER_MATERIALS(R.id.nav_customer_materials, "Materials", R.drawable.ic_material),
//        CUSTOMER_SHOPS(R.id.nav_customer_shops, "Nearby Shops", R.drawable.ic_location),
        CUSTOMER_CART(R.id.nav_customer_cart, "Cart", R.drawable.ic_cart),
        CUSTOMER_WISHLIST(R.id.nav_customer_wishlist, "Wishlist", R.drawable.ic_wishlist),
        CUSTOMER_PROFILE(R.id.nav_customer_profile, "Profile", R.drawable.ic_profile)
    }

    fun setupAdminMenu(
        navigationView: NavigationView,
        drawerLayout: DrawerLayout,
        onOptionSelected: (Option) -> Unit
    ) {
        val menu = navigationView.menu
        menu.clear()

        val adminOptions = listOf(
            Option.ADMIN_DASHBOARD,
            Option.ADMIN_WORKERS,
//            Option.ADMIN_SHOPS,
            Option.ADMIN_INVENTORY,
            Option.ADMIN_SEARCH,
            Option.ADMIN_PROFILE
        )

        for (opt in adminOptions) {
            menu.add(0, opt.id, menu.size, opt.title).setIcon(opt.iconRes)
        }

        navigationView.setNavigationItemSelectedListener { item ->
            Option.entries.firstOrNull { it.id == item.itemId }?.let(onOptionSelected)
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    fun setupCustomerMenu(
        navigationView: NavigationView,
        drawerLayout: DrawerLayout,
        onOptionSelected: (Option) -> Unit
    ) {
        val menu = navigationView.menu
        menu.clear()

        val customerOptions = listOf(
            Option.CUSTOMER_DASHBOARD,
            Option.CUSTOMER_CATEGORIES,
            Option.CUSTOMER_MATERIALS,
//            Option.CUSTOMER_SHOPS,
            Option.CUSTOMER_CART,
            Option.CUSTOMER_WISHLIST,
            Option.CUSTOMER_PROFILE
        )

        for (opt in customerOptions) {
            menu.add(0, opt.id, menu.size, opt.title).setIcon(opt.iconRes)
        }

        navigationView.setNavigationItemSelectedListener { item ->
            Option.entries.firstOrNull { it.id == item.itemId }?.let(onOptionSelected)
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }
}
