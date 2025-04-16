package com.example.mytailorsapp.ui.admin

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mytailorsapp.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProfileScreen(
    navController: NavController,
    viewModel: AdminViewModel)
{
    val context = LocalContext.current
    val admin = viewModel.adminDetails

    LaunchedEffect(Unit) {
        viewModel.fetchLoggedInAdmin()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Admin Profile") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Admin Profile Details", style = MaterialTheme.typography.titleLarge)

            if (admin != null) {
                // ✅ Username field (read-only)
                OutlinedTextField(
                    value = admin.username,
                    onValueChange = {},
                    label = { Text("Username") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // ✅ Password field (read-only)
                OutlinedTextField(
                    value = admin.password,
                    onValueChange = {},
                    label = { Text("Password") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text("Loading admin data...", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logout Button
            Button(
                onClick = {
                    viewModel.logout()
                    Toast.makeText(context, "Logged Out Successfully", Toast.LENGTH_SHORT).show()
                    navController.navigate("loginScreen") { popUpTo("profileScreen") { inclusive = true } }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Logout")
            }
        }
    }
}
