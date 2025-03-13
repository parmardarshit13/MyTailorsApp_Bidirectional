package com.example.mytailorsapp.ui.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mytailorsapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialScreen(navController: NavController) {
    val materials = listOf(
        MaterialItem("Cotton", "Soft and breathable", "₹500/meter", R.drawable.cotton),
        MaterialItem("Silk", "Smooth and shiny", "₹1200/meter", R.drawable.silk),
        MaterialItem("Denim", "Strong and durable", "₹800/meter", R.drawable.denim)
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Choose Material") }) }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(materials) { material ->
                MaterialCard(material, navController)
            }
        }
    }
}

@Composable
fun MaterialCard(material: MaterialItem, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(painter = painterResource(id = material.image), contentDescription = material.name)
            Text("Name: ${material.name}", style = MaterialTheme.typography.titleLarge)
            Text("Type: ${material.description}")
            Text("Price: ${material.price}")

            // 🔹 Pass both material name & image ID as arguments
            Button(onClick = {
                navController.navigate("virtual_clothing_screen/${material.name}/${material.image}")
            }) {
                Text("Preview in Virtual Mode")
            }
        }
    }
}

data class MaterialItem(val name: String, val description: String, val price: String, val image: Int)
