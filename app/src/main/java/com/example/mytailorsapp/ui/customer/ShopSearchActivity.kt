package com.example.mytailorsapp.ui.customer

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.models.TailorShop
import com.example.mytailorsapp.databinding.ActivityShopSearchBinding
import com.example.mytailorsapp.ui.adapter.TailorShopAdapter
import com.example.mytailorsapp.ui.common.CustomerNavigationHelper
import com.example.mytailorsapp.ui.common.SidebarMenuHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.navigation.NavigationView

class ShopSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShopSearchBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var placesClient: PlacesClient
    private val shops = mutableListOf<TailorShop>()
    private lateinit var adapter: TailorShopAdapter

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        navigationView = binding.navView
        toolbar = binding.toolbar

        setSupportActionBar(toolbar)

        // ✅ Make the custom icon clickable
        val navIcon: ImageView = findViewById(R.id.custom_nav_icon)
        navIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Populate menu and handle clicks
        SidebarMenuHelper.setupCustomerMenu(navigationView, drawerLayout) { option ->
            CustomerNavigationHelper.handleNavigation(this, option)
        }

        // Initialize Places
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_maps_key))
        }
        placesClient = Places.createClient(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // RecyclerView setup
        adapter = TailorShopAdapter(shops)
        binding.recyclerViewShops.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewShops.adapter = adapter

        // Check location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
        } else {
            fetchLocationAndShops()
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun fetchLocationAndShops() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                queryNearbyTailors(location)
            } else {
                Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun queryNearbyTailors(location: Location) {
        // Define a small bounding box (~5km radius)
        val bounds = RectangularBounds.newInstance(
            LatLng(location.latitude - 0.05, location.longitude - 0.05),
            LatLng(location.latitude + 0.05, location.longitude + 0.05)
        )
        // Only request name & address
        val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS)
        val request = FindCurrentPlaceRequest.newInstance(placeFields)
        placesClient.findCurrentPlace(request).addOnSuccessListener { response ->
            shops.clear()
            for (likelihood in response.placeLikelihoods) {
                val place = likelihood.place
                // Simple filter for “tailor” in the name
                if (place.name?.contains("Tailor", true) == true) {
                    shops.add(
                        TailorShop(
                            name = place.name ?: "Unknown",
                            location = place.address ?: "Unknown",
                            pricePerItem = "N/A"
                        )
                    )
                }
            }
            adapter.notifyDataSetChanged()
            if (shops.isEmpty()) {
                Toast.makeText(this, "No tailor shops found nearby", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Places API error: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            fetchLocationAndShops()
        } else {
            Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show()
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
