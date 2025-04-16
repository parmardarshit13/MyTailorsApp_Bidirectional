package com.example.mytailorsapp.ui.customer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mytailorsapp.data.models.MaterialItem
import com.example.mytailorsapp.ui.common.SidebarDrawer
import com.example.mytailorsapp.viewmodel.CustomerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialScreen(
    navController: NavController,
    category: String,
    viewModel: CustomerViewModel,
    userId: Int = 0,
    isDark: Boolean,
    onToggleTheme: () -> Unit
) {
    val materials by viewModel.materialItems.collectAsState(initial = emptyList())
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableIntStateOf(0) }

    val filteredMaterials = remember(materials, category) {
        materials.filter { it.category.equals(category, ignoreCase = true) }
            .distinctBy { it.name }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SidebarDrawer(
                drawerState = drawerState,
                navController = navController,
                selectedIndex = selectedItem,
                onSelect = { selectedItem = it },
                scope = scope,
                isDark = isDark,
                onToggleTheme = onToggleTheme,
                userId = userId,
                isAdminDashboard = false
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("$category Materials") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { paddingValues ->
            if (filteredMaterials.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No materials available for $category")
                }
            } else {
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
            // ✅ Load image from Firebase URL
            AsyncImage(
                model = material.image,
                contentDescription = material.name,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Text("Name: ${material.name}", style = MaterialTheme.typography.titleLarge)
            Text("Description: ${material.description}")
            Text("Price: ${material.price}")
            Text("Category: ${material.category}")

            Button(
                onClick = {
                    navController.navigate(
                        "virtual_clothing_screen/${material.name}/${material.image}"
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Preview in Virtual Mode")
            }
        }
    }
}
