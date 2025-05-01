package com.example.mytailorsapp.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.models.CustomerEntity
import com.example.mytailorsapp.data.repository.CustomerRepository
import kotlinx.coroutines.*
import android.graphics.drawable.AnimationDrawable

class RegisterActivity : AppCompatActivity() {

    private lateinit var repository: CustomerRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // 🌈 Setup animated background
        val root = findViewById<ScrollView>(R.id.register_root)
        val animationDrawable = root.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()

        repository = CustomerRepository()

        val nameField = findViewById<EditText>(R.id.etName)
        val contactField = findViewById<EditText>(R.id.etContact)
        val emailField = findViewById<EditText>(R.id.etEmail)
        val addressField = findViewById<EditText>(R.id.etAddress)
        val passwordField = findViewById<EditText>(R.id.etPassword)
        val confirmPasswordField = findViewById<EditText>(R.id.etConfirmPassword)
        val userTypeSpinner = findViewById<Spinner>(R.id.spinnerUserType)
        val registerBtn = findViewById<Button>(R.id.btnRegister)
        val snackbar = findViewById<TextView>(R.id.tvSnackbar)

        registerBtn.setOnClickListener {
            val name = nameField.text.toString().trim()
            val contact = contactField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val address = addressField.text.toString().trim()
            val password = passwordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()
            val userType = userTypeSpinner.selectedItem.toString()

            CoroutineScope(Dispatchers.Main).launch {
                snackbar.text = ""

                if (name.isEmpty() || contact.isEmpty() || email.isEmpty() || address.isEmpty() || password.isEmpty()) {
                    snackbar.text = "All fields are required!"
                    return@launch
                }

                if (!contact.matches(Regex("^[0-9]{10}$"))) {
                    snackbar.text = "Enter a valid 10-digit contact number"
                    return@launch
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    snackbar.text = "Enter a valid email"
                    return@launch
                }

                if (password.length < 6) {
                    snackbar.text = "Password must be at least 6 characters"
                    return@launch
                }

                if (password != confirmPassword) {
                    snackbar.text = "Passwords do not match"
                    return@launch
                }

                val existing = withContext(Dispatchers.IO) {
                    repository.getCustomerByEmail(email)
                }

                if (existing != null) {
                    snackbar.text = "Email already registered!"
                    return@launch
                }

                val newCustomer = CustomerEntity(
                    name = name,
                    contact = contact,
                    email = email,
                    address = address,
                    password = password,
                    userType = userType,
                    isLoggedIn = false
                )

                val result = withContext(Dispatchers.IO) {
                    repository.registerCustomer(newCustomer) // ✅ This handles document ID assignment
                }

                if (result) {
                    Toast.makeText(this@RegisterActivity, "Registration Successful!", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this@RegisterActivity, "Registration Failed! Try again.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
