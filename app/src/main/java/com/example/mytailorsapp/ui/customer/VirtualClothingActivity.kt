package com.example.mytailorsapp.ui.customer

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.mytailorsapp.R
import com.example.mytailorsapp.ui.common.CustomerNavigationHelper
import com.example.mytailorsapp.ui.common.SidebarMenuHelper
import com.google.android.material.navigation.NavigationView

class VirtualClothingActivity : AppCompatActivity() {

    private lateinit var silhouetteImage: ImageView
    private lateinit var overlayImage: ImageView

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_virtual_clothing)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)

        // Populate menu and handle clicks
        SidebarMenuHelper.setupCustomerMenu(navigationView, drawerLayout) { option ->
            CustomerNavigationHelper.handleNavigation(this, option)
        }

        silhouetteImage = findViewById(R.id.imgSilhouette)
        overlayImage = findViewById(R.id.imgClothingOverlay)

        // Optional: Load different overlays dynamically from Firebase or assets
        val clothingDrawable = ContextCompat.getDrawable(this, R.drawable.sample_tshirt_overlay)
        overlayImage.setImageDrawable(clothingDrawable)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
