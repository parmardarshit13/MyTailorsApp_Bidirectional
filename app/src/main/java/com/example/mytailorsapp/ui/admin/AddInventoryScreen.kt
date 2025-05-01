package com.example.mytailorsapp.ui.admin

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.models.InventoryItem
import com.example.mytailorsapp.data.models.InventoryStatus
import com.example.mytailorsapp.data.repository.InventoryRepository
import com.example.mytailorsapp.viewmodel.InventoryViewModel
import com.example.mytailorsapp.viewmodel.InventoryViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddInventoryActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var customerName: EditText
    private lateinit var shirtQty: EditText
    private lateinit var pantQty: EditText
    private lateinit var workerName: EditText
    private lateinit var notes: EditText
    private lateinit var deadlineField: EditText
    private lateinit var pricePerItem: EditText
    private lateinit var itemStatusSpinner: Spinner
    private lateinit var addItemButton: Button
    private lateinit var cancelButton: Button

    private lateinit var viewModel: InventoryViewModel
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_inventory)

        drawerLayout = findViewById(R.id.drawer_layout)
        customerName = findViewById(R.id.customer_name)
        shirtQty = findViewById(R.id.shirt_quantity)
        pantQty = findViewById(R.id.pant_quantity)
        workerName = findViewById(R.id.worker_name)
        notes = findViewById(R.id.inventory_notes)
        deadlineField = findViewById(R.id.item_deadline)
        pricePerItem = findViewById(R.id.price_per_item)
        itemStatusSpinner = findViewById(R.id.item_status)
        addItemButton = findViewById(R.id.add_item_button)
        cancelButton = findViewById(R.id.btnCancel)

        viewModel = ViewModelProvider(
            this,
            InventoryViewModelFactory(InventoryRepository())
        )[InventoryViewModel::class.java]


        // Populate status spinner
        val statusAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            InventoryStatus.entries.map { it.name }
        )
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        itemStatusSpinner.adapter = statusAdapter

        // Date picker for deadline
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        deadlineField.setOnClickListener {
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                calendar.set(year, month, day)
                deadlineField.setText(dateFormatter.format(calendar.time))
            }
            DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Enable Add button only if inputs are valid
        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                addItemButton.isEnabled = validateInputs()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        arrayOf(
            customerName, shirtQty,
            pantQty, workerName, deadlineField
        ).forEach { it.addTextChangedListener(watcher) }

        addItemButton.setOnClickListener {
            val customer = customerName.text.toString()
            val shirt = shirtQty.text.toString().toIntOrNull() ?: 0
            val pant = pantQty.text.toString().toIntOrNull() ?: 0
            val worker = workerName.text.toString()
            val price = pricePerItem.text.toString().toDoubleOrNull() ?: 0.0
            val noteText = notes.text.toString()
            val deadline = calendar.time
            val status = InventoryStatus.valueOf(itemStatusSpinner.selectedItem.toString())

            val newItem = InventoryItem(
                customerName = customer,
                workerName = worker,
                itemDescription = noteText,
                quantity = shirt + pant, // or ask for a separate total qty field
                totalPrice = price * (shirt + pant),
                status = status,
                deadline = deadline
            )

            lifecycleScope.launch {
                viewModel.addInventoryItem(newItem)
                Toast.makeText(this@AddInventoryActivity, "Inventory added", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        cancelButton.setOnClickListener {
            startActivity(Intent(this, AdminManageInventoryActivity::class.java))
            finish()
        }
    }

    private fun validateInputs(): Boolean {
        return  customerName.text.isNotEmpty() &&
                shirtQty.text.isNotEmpty() &&
                pantQty.text.isNotEmpty() &&
                workerName.text.isNotEmpty() &&
                pricePerItem.text.isNotEmpty() &&
                deadlineField.text.isNotEmpty()
    }
}
