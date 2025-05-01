package com.example.mytailorsapp.ui.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.repository.InventoryRepository
import com.example.mytailorsapp.ui.adapter.InventoryAdapter
import com.example.mytailorsapp.viewmodel.InventoryViewModel
import com.example.mytailorsapp.viewmodel.InventoryViewModelFactory

class SearchInventoryActivity : AppCompatActivity() {

    private lateinit var searchField: EditText
    private lateinit var searchButton: Button
    private lateinit var cancelButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: InventoryAdapter
    private lateinit var viewModel: InventoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_inventory)

        // Init views
        searchField = findViewById(R.id.etSearchName)
        searchButton = findViewById(R.id.btnSearch)
        cancelButton = findViewById(R.id.btnCancel)
        recyclerView = findViewById(R.id.recyclerViewInventoryResults)
        progressBar = findViewById(R.id.progressBar)

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = InventoryAdapter(
            onItemClick = { /* You can show details screen if needed */ },
            onItemLongClick = { /* Optional long click behavior */ }
        )
        recyclerView.adapter = adapter

        // Init ViewModel
        viewModel = ViewModelProvider(
            this,
            InventoryViewModelFactory(InventoryRepository())
        )[InventoryViewModel::class.java]

        // Observe inventory results
        viewModel.inventoryItems.observe(this) { items ->
            progressBar.visibility = View.GONE
            adapter.submitList(items)
            if (items.isEmpty()) {
                Toast.makeText(this, "No inventory found", Toast.LENGTH_SHORT).show()
            }
        }

        // Search
        searchButton.setOnClickListener {
            val name = searchField.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter a customer name", Toast.LENGTH_SHORT).show()
            } else {
                progressBar.visibility = View.VISIBLE
                viewModel.searchByCustomerName(name)
            }
        }

        // Cancel button
        cancelButton.setOnClickListener {
            startActivity(Intent(this, AdminManageInventoryActivity::class.java))
            finish()
        }
    }
}
