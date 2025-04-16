package com.example.mytailorsapp.ui.customer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mytailorsapp.data.models.Category
import com.example.mytailorsapp.data.repository.CategoryRepository
import com.example.mytailorsapp.ui.common.SidebarDrawer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    navController: NavController,
    userId: Int = 0,
    isDark: Boolean,
    onToggleTheme: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableIntStateOf(0) }

    val repository = remember { CategoryRepository() }
    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }

    LaunchedEffect(Unit) {
        categories = repository.getAllCategories()
    }

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
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Select Clothing Category") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
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
                items(categories) { category ->
                    CategoryItem(category.name, category.imageUrl) {
                        navController.navigate("material_screen/${category.name}")
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(categoryName: String, imageUrl: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = imageUrl,
                contentDescription = categoryName,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = categoryName,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}