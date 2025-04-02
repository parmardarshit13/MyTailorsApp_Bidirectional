package com.example.mytailorsapp.ui.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mytailorsapp.R
import com.example.mytailorsapp.database.MaterialItem
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytailorsapp.viewmodel.CustomerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialScreen(navController: NavController, category: String) {
    val viewModel: CustomerViewModel = viewModel()
    val materials by viewModel.materialItems.collectAsState(initial = emptyList())

    val filteredMaterials = materials.filter { it.category == category }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Choose Material for $category") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            items(filteredMaterials) { material ->
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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = material.image),
                contentDescription = material.name,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Text(
                "Name: ${material.name}",
                style = MaterialTheme.typography.titleLarge
            )
            Text("Type: ${material.description}")
            Text("Price: ${material.price}")

            Button(
                onClick = {
                    navController.navigate(
                        "virtual_clothing_screen/${material.name}/${material.image}"
                    ) {
                        launchSingleTop = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Preview in Virtual Mode")
            }
        }
    }
}
