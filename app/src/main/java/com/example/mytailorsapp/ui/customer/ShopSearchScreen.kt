package com.example.mytailorsapp.ui.customer

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mytailorsapp.ui.common.SidebarDrawer

data class TailorShop(val name: String, val location: String, val pricePerItem: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopSearchScreen(
    navController: NavController,
    isDark: Boolean,
    userId: Int = 0,
    onToggleTheme: () -> Unit
) {
    val context = LocalContext.current

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableIntStateOf(0) }

    val tailorShops = listOf(
        TailorShop("Perfect Stitch", "Downtown", "$5 per item"),
        TailorShop("Elite Tailors", "Uptown", "$8 per item"),
        TailorShop("Classic Fits", "Midtown", "$6 per item"),
        TailorShop("Royal Tailors", "Westside", "$7 per item"),
        TailorShop("Modern Tailors", "Eastside", "$9 per item")
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // ✅ Use centralized sidebar here
            SidebarDrawer(
                drawerState = drawerState,
                navController = navController,
                selectedIndex = selectedItem,
                onSelect = { selectedItem = it },
                scope = scope,
                isDark = isDark,
                onToggleTheme = onToggleTheme,
                userId = userId,
                isAdminDashboard = false // ✅ default for customers
            )
        }
    ){
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Find Tailor Shops") })
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text("Select a Tailor Shop", style = MaterialTheme.typography.titleLarge)

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tailorShops) { shop ->
                        TailorShopCard(shop) {
                            Toast.makeText(context, "Selected: ${shop.name}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TailorShopCard(shop: TailorShop, onSelect: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Shop: ${shop.name}", style = MaterialTheme.typography.titleLarge)
            Text("Location: ${shop.location}")
            Text("Price: ${shop.pricePerItem}")
        }
    }
}
