package com.example.mytailorsapp.ui.admin

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.models.InventoryItem
import com.example.mytailorsapp.data.repository.InventoryRepository
import com.example.mytailorsapp.ui.adapter.InventoryAdapter
import com.example.mytailorsapp.viewmodel.InventoryViewModel
import kotlinx.coroutines.launch

class DeleteInventoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: InventoryAdapter

    private val repository = InventoryRepository()
    private val viewModel = InventoryViewModel(repository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_inventory)

        recyclerView = findViewById(R.id.recyclerViewDeleteInventory)
        progressBar = findViewById(R.id.deleteProgressBar)

        // Set up adapter with a click listener that shows a confirmation dialog
        adapter = InventoryAdapter(
            onItemClick = { },
            onItemLongClick = { item ->
            AlertDialog.Builder(this)
                .setTitle("Delete Item for “${item.customerName}”?")
                .setMessage("This action cannot be undone.")
                .setPositiveButton("Delete") { _, _ ->
                    deleteItem(item)
                }
                .setNegativeButton("Cancel", null)
                .show()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadInventory()
    }

    private fun loadInventory() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val list = repository.getAllAdminInventory()
                if (list.isEmpty()) {
                    Toast.makeText(this@DeleteInventoryActivity, "No inventory items to delete", Toast.LENGTH_SHORT).show()
                }
                adapter.submitList(list)
            } catch (e: Exception) {
                Toast.makeText(this@DeleteInventoryActivity, "Failed to load items: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun deleteItem(item: InventoryItem) {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                viewModel.deleteInventoryItem(item)
                Toast.makeText(this@DeleteInventoryActivity, "Deleted Item for “${item.customerName}”", Toast.LENGTH_SHORT).show()
                loadInventory()  // refresh the list
            } catch (e: Exception) {
                Toast.makeText(this@DeleteInventoryActivity, "Delete failed: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}
