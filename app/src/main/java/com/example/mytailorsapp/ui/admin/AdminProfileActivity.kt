package com.example.mytailorsapp.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.repository.AdminRepository
import com.example.mytailorsapp.ui.auth.LoginActivity
import com.example.mytailorsapp.ui.common.AdminNavigationHelper
import com.example.mytailorsapp.ui.common.SidebarMenuHelper
import com.example.mytailorsapp.viewmodel.AdminViewModel
import com.example.mytailorsapp.viewmodel.AdminViewModelFactory
import com.google.android.material.navigation.NavigationView

class AdminProfileActivity : AppCompatActivity() {

    private lateinit var viewModel: AdminViewModel
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var logoutButton: Button

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_profile)

        viewModel = AdminViewModelFactory(AdminRepository()).create(AdminViewModel::class.java)
        usernameField = findViewById(R.id.etUsername)
        passwordField = findViewById(R.id.etPassword)
        logoutButton = findViewById(R.id.btnLogout)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView       = findViewById(R.id.nav_view)
        toolbar       = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        // ✅ Make the custom icon clickable
        val navIcon: ImageView = findViewById(R.id.custom_nav_icon)
        navIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Populate menu and handle clicks
        SidebarMenuHelper.setupAdminMenu(navView, drawerLayout) { option ->
            AdminNavigationHelper.handleNavigation(this, option)
        }

        // Load admin details
        viewModel.fetchLoggedInAdmin()

        viewModel.adminDetails.observe(this) { admin ->
            if (admin != null) {
                usernameField.setText(admin.username)
                passwordField.setText(admin.password)
            } else {
                Toast.makeText(this, "Failed to load admin data", Toast.LENGTH_SHORT).show()
            }
        }

        logoutButton.setOnClickListener {
            viewModel.logout()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
