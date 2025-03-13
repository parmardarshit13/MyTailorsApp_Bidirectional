package com.example.mytailorsapp.ui.customer

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class TailorShop(val name: String, val location: String, val pricePerItem: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopSearchScreen(navController: NavController) {
    val context = LocalContext.current

    val tailorShops = listOf(
        TailorShop("Perfect Stitch", "Downtown", "$5 per item"),
        TailorShop("Elite Tailors", "Uptown", "$8 per item"),
        TailorShop("Classic Fits", "Midtown", "$6 per item"),
        TailorShop("Royal Tailors", "Westside", "$7 per item"),
        TailorShop("Modern Tailors", "Eastside", "$9 per item")
    )

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
