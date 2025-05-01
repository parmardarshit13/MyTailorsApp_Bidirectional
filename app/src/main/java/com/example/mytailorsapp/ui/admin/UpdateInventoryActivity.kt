package com.example.mytailorsapp.ui.admin

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.models.InventoryItem
import com.example.mytailorsapp.data.models.InventoryStatus
import com.example.mytailorsapp.data.repository.InventoryRepository
import com.example.mytailorsapp.viewmodel.InventoryViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class UpdateInventoryActivity : AppCompatActivity() {

    private lateinit var etCustomerName: EditText
    private lateinit var etWorkerName: EditText
    private lateinit var etDescription: EditText
    private lateinit var etQuantity: EditText
    private lateinit var etPricePerItem: EditText  // ⚠️ this holds pricePerItem value only
    private lateinit var etDeadline: EditText
    private lateinit var spinnerStatus: Spinner
    private lateinit var btnUpdateItem: Button

    private lateinit var viewModelItem: InventoryItem
    private var selectedDate: Date? = null
    private val repository = InventoryRepository()
    private val viewModel = InventoryViewModel(repository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_inventory)

        // Initialize views
        etCustomerName = findViewById(R.id.etCustomerName)
        etWorkerName = findViewById(R.id.etWorkerName)
        etPricePerItem = findViewById(R.id.etPricePerItem)
        etDescription = findViewById(R.id.etDescription)
        etQuantity = findViewById(R.id.etQuantity)
        etDeadline = findViewById(R.id.etDeadline)
        spinnerStatus = findViewById(R.id.spinnerStatus)
        btnUpdateItem = findViewById(R.id.btnUpdateItem)

        // Setup dropdown
        val statusAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            InventoryStatus.entries.map { it.name }
        )
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = statusAdapter

        // Get item
        val item = intent.getSerializableExtra("inventoryItem") as? InventoryItem
        if (item == null) {
            Toast.makeText(this, "No item data provided", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        viewModelItem = item
        populateFields(item)

        // Date Picker
        etDeadline.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dialog = DatePickerDialog(
                this,
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    selectedDate = calendar.time
                    etDeadline.setText(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate!!))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show()
        }

        // Handle update
        btnUpdateItem.setOnClickListener {
            val customerName = etCustomerName.text.toString().trim()
            val workerName = etWorkerName.text.toString().trim()
            val pricePerItem = etPricePerItem.text.toString().toDoubleOrNull() ?: 0.0
            val description = etDescription.text.toString().trim()
            val quantity = etQuantity.text.toString().toIntOrNull() ?: 0
            val status = InventoryStatus.valueOf(spinnerStatus.selectedItem.toString())

            if (customerName.isNotEmpty() && quantity > 0) {
                val updatedItem = viewModelItem.copy(
                    customerName = customerName,
                    workerName = workerName,
                    itemDescription = description,
                    quantity = quantity,
                    totalPrice = pricePerItem * quantity, // ✅ recalculate
                    deadline = selectedDate ?: viewModelItem.deadline,
                    status = status
                )

                lifecycleScope.launch {
                    try {
                        viewModel.updateInventoryItem(updatedItem)
                        Toast.makeText(this@UpdateInventoryActivity, "Inventory updated successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } catch (e: Exception) {
                        Toast.makeText(this@UpdateInventoryActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun populateFields(item: InventoryItem) {
        etCustomerName.setText(item.customerName)
        etWorkerName.setText(item.workerName)
        etDescription.setText(item.itemDescription)
        etQuantity.setText(item.quantity.toString())

        val pricePerItem = if (item.quantity > 0) item.totalPrice / item.quantity else 0.0
        etPricePerItem.setText(String.format("%.2f", pricePerItem))

        etDeadline.setText(item.deadline?.let {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
        } ?: "")

        spinnerStatus.setSelection(InventoryStatus.entries.indexOf(item.status))
    }
}
