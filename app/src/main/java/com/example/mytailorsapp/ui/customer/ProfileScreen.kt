package com.example.mytailorsapp.ui.customer

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mytailorsapp.viewmodel.CustomerViewModel
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mytailorsapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    customerId: Int,
    viewModel: CustomerViewModel = viewModel()
) {
    val customer by viewModel.selectedCustomer.collectAsState()

    // Fetch customer data on screen load
    LaunchedEffect(customerId) {
        viewModel.fetchCustomerById(customerId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val context = LocalContext.current
            val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    viewModel.updateProfileImage(customerId, it, context)
                }
            }
            Text(
                "Profile Information",
                style = MaterialTheme.typography.headlineSmall
            )

            // ✅ Profile Image
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clickable { imagePicker.launch("image/*") }
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                if (!customer?.profileImageUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(customer?.profileImageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Profile Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_add_photo), // ✅ Replace with your default icon
                        contentDescription = "Add Photo",
                        tint = Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            customer?.let { user ->
                ProfileDetailItem(label = "Full Name", value = user.name)
                ProfileDetailItem(label = "Phone Number", value = user.contact)
                ProfileDetailItem(label = "Email", value = user.email)
                ProfileDetailItem(label = "Address", value = user.address)
            }

            // Update Profile Button
            Button(
                onClick = { navController.navigate("update_profile_screen/$customerId") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update Profile")
            }

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

@Composable
fun ProfileDetailItem(label: String, value: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelLarge)
        Text(value, style = MaterialTheme.typography.bodyLarge)
        HorizontalDivider()
    }
}
