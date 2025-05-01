package com.example.mytailorsapp.ui.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.models.InventoryItem
import com.example.mytailorsapp.data.repository.InventoryRepository
import com.example.mytailorsapp.ui.adapter.InventoryAdapter
import com.example.mytailorsapp.ui.common.AdminNavigationHelper
import com.example.mytailorsapp.ui.common.SidebarMenuHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class AdminManageInventoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var inventoryAdapter: InventoryAdapter
    private lateinit var fab: FloatingActionButton

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toolbar: Toolbar

    private lateinit var inventoryList: List<InventoryItem>

    private val repository = InventoryRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_manage_inventory)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView       = findViewById(R.id.nav_view)
        toolbar       = findViewById(R.id.toolbar)

        recyclerView = findViewById(R.id.recyclerViewInventory)
        progressBar = findViewById(R.id.progressBar)
        fab = findViewById(R.id.fabInventoryActions)

        recyclerView.layoutManager = LinearLayoutManager(this)
        inventoryAdapter = InventoryAdapter(
            onItemClick = { /* Can be set for detail activity */ },
            onItemLongClick = { item -> showOptionsDialog(item) }
        )
        recyclerView.adapter = inventoryAdapter

        setSupportActionBar(toolbar)

        // 🔷 Menu icon opens drawer
        findViewById<ImageView>(R.id.custom_nav_icon).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // 🔍 Search icon redirects to search activity
        findViewById<ImageView>(R.id.custom_search_icon).setOnClickListener {
            startActivity(Intent(this, SearchInventoryActivity::class.java))
        }

        // 🧮 Sort icon opens popup menu
        findViewById<ImageView>(R.id.custom_sort_icon).setOnClickListener { view ->
            showSortMenu(view)
        }

        SidebarMenuHelper.setupAdminMenu(navView, drawerLayout) { option ->
            AdminNavigationHelper.handleNavigation(this, option)
        }

        fab.setOnClickListener {
            startActivity(Intent(this, AddInventoryActivity::class.java))
        }

        loadInventory()
    }

    override fun onResume() {
        super.onResume()
        loadInventory()
    }

    private fun loadInventory() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                inventoryList = repository.getAllAdminInventory()
                if (inventoryList.isEmpty()) {
                    Toast.makeText(this@AdminManageInventoryActivity, "No inventory found", Toast.LENGTH_SHORT).show()
                }
                inventoryAdapter.submitList(inventoryList)
            } catch (e: Exception) {
                Toast.makeText(this@AdminManageInventoryActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun showOptionsDialog(item: InventoryItem) {
        val options = arrayOf("Update", "Delete")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Select an action")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> {
                    val intent = Intent(this, UpdateInventoryActivity::class.java)
                    intent.putExtra("inventoryItem", item)
                    startActivity(intent)
                }
                1 -> {
                    lifecycleScope.launch {
                        try {
                            repository.deleteInventoryItem(item)
                            Toast.makeText(this@AdminManageInventoryActivity, "Deleted successfully", Toast.LENGTH_SHORT).show()
                            loadInventory()
                        } catch (e: Exception) {
                            Toast.makeText(this@AdminManageInventoryActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
        builder.show()
    }

    private fun showSortMenu(view: View) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.menu_inventory_sort, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sort_deadline_asc -> {
                    sortInventoryByDeadline(true)
                    true
                }
                R.id.sort_deadline_desc -> {
                    sortInventoryByDeadline(false)
                    true
                }
                R.id.sort_by_customer -> {
                    sortInventoryByCustomer()
                    true
                }
                R.id.sort_by_status -> {
                    sortInventoryByStatus()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun sortInventoryByDeadline(ascending: Boolean) {
        val sorted = if (ascending) {
            inventoryList.sortedBy { it.deadline }
        } else {
            inventoryList.sortedByDescending { it.deadline }
        }
        inventoryAdapter.submitList(sorted)
    }

    private fun sortInventoryByCustomer() {
        inventoryAdapter.submitList(inventoryList.sortedBy { it.customerName })
    }

    private fun sortInventoryByStatus() {
        inventoryAdapter.submitList(inventoryList.sortedBy { it.status.name })
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
