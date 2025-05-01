package com.example.mytailorsapp.ui.auth

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.edit
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.repository.*
import com.example.mytailorsapp.ui.admin.AdminDashboardActivity
import com.example.mytailorsapp.ui.customer.CustomerDashboardActivity
import com.example.mytailorsapp.viewmodel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginButton: Button
    private lateinit var registerText: TextView
    private lateinit var forgotPasswordText: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var customerViewModel: CustomerViewModel
    private lateinit var adminViewModel: AdminViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 🎨 Animated background
        val root = findViewById<ConstraintLayout>(R.id.login_root)
        val animationDrawable = root.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()

        // 🎯 UI components
        emailField = findViewById(R.id.editTextEmail)
        passwordField = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.btnLogin)
        registerText = findViewById(R.id.tvRegister)
        forgotPasswordText = findViewById(R.id.tvForgotPassword)
        progressBar = findViewById(R.id.loginProgressBar)

        // 📦 ViewModels
        customerViewModel = CustomerViewModelFactory(
            CustomerRepository(),
            MaterialRepository(),
            CartRepository(),
            WishlistRepository()
        ).create(CustomerViewModel::class.java)

        adminViewModel = AdminViewModelFactory(AdminRepository())
            .create(AdminViewModel::class.java)

        // 🔐 Login button logic
        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = ProgressBar.VISIBLE

            // 🔑 Try Admin first
            adminViewModel.authenticateAdmin(email, password) { isAdmin ->
                if (isAdmin == true) {
                    progressBar.visibility = ProgressBar.GONE
                    startActivity(Intent(this, AdminDashboardActivity::class.java))
                    finish()
                } else {
                    // 👤 Try Customer login
                    CoroutineScope(Dispatchers.Main).launch {
                        val customer = customerViewModel.login(email, password)
                        if (customer != null) {
                            // 🧠 Save Fire store-generated ID to SharedPreferences
                            val sharedPref = getSharedPreferences("MyTailorPrefs", MODE_PRIVATE)
                            sharedPref.edit {
                                putString("loggedInCustomerId", customer.id)
                            }

                            progressBar.visibility = ProgressBar.GONE
                            startActivity(Intent(this@LoginActivity, CustomerDashboardActivity::class.java))
                            finish()
                        } else {
                            progressBar.visibility = ProgressBar.GONE
                            Toast.makeText(this@LoginActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        // 🔗 Navigation
        registerText.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        forgotPasswordText.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }
}
