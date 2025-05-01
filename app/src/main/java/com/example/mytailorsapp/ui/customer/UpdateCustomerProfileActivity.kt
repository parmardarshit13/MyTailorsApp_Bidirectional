package com.example.mytailorsapp.ui.customer

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.models.CustomerEntity
import com.example.mytailorsapp.data.repository.CartRepository
import com.example.mytailorsapp.data.repository.CustomerRepository
import com.example.mytailorsapp.data.repository.MaterialRepository
import com.example.mytailorsapp.data.repository.WishlistRepository
import com.example.mytailorsapp.viewmodel.CustomerViewModel
import com.example.mytailorsapp.viewmodel.CustomerViewModelFactory
import kotlinx.coroutines.launch

class UpdateCustomerProfileActivity : AppCompatActivity() {

    private lateinit var viewModel: CustomerViewModel
    private lateinit var nameField: EditText
    private lateinit var contactField: EditText
    private lateinit var emailField: EditText
    private lateinit var addressField: EditText
    private lateinit var passwordField: EditText
    private lateinit var confirmPasswordField: EditText
    private lateinit var btnSave: Button

    private var customerId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_customer_profile)

        customerId = intent.getStringExtra("customerId").toString()

        viewModel = CustomerViewModelFactory(
            CustomerRepository(),
            MaterialRepository(),
            CartRepository(),
            WishlistRepository()
        ).create(CustomerViewModel::class.java)

        nameField = findViewById(R.id.etName)
        contactField = findViewById(R.id.etContact)
        emailField = findViewById(R.id.etEmail)
        addressField = findViewById(R.id.etAddress)
        passwordField = findViewById(R.id.etPassword)
        confirmPasswordField = findViewById(R.id.etConfirmPassword)
        btnSave = findViewById(R.id.btnSave)

        viewModel.fetchCustomerById(customerId)

        viewModel.selectedCustomer.observe(this) { customer ->
            customer?.let {
                nameField.setText(it.name)
                contactField.setText(it.contact)
                emailField.setText(it.email)
                addressField.setText(it.address)
                passwordField.setText(it.password)
            }
        }

        btnSave.setOnClickListener {
            val name = nameField.text.toString()
            val contact = contactField.text.toString()
            val email = emailField.text.toString()
            val address = addressField.text.toString()
            val password = passwordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailField.error = "Invalid email"
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                confirmPasswordField.error = "Passwords do not match"
                return@setOnClickListener
            }

            val updatedCustomer = CustomerEntity(
                id = customerId,
                name = name,
                contact = contact,
                email = email,
                address = address,
                password = password,
                isLoggedIn = true
            )

            lifecycleScope.launch {
                viewModel.updateCustomerProfile(updatedCustomer)
                Toast.makeText(this@UpdateCustomerProfileActivity, "Profile Updated!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
