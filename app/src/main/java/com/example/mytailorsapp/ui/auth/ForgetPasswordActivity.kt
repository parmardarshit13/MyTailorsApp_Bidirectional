package com.example.mytailorsapp.ui.auth

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.repository.CustomerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var repository: CustomerRepository
    private lateinit var emailField: EditText
    private lateinit var newPasswordField: EditText
    private lateinit var confirmPasswordField: EditText
    private lateinit var resetButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var feedbackText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        // 🌈 Setup animated background
        val root = findViewById<ScrollView>(R.id.forget_password_root)
        val animationDrawable = root.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()

        repository = CustomerRepository()

        emailField = findViewById(R.id.etEmail)
        newPasswordField = findViewById(R.id.etNewPassword)
        confirmPasswordField = findViewById(R.id.etConfirmPassword)
        resetButton = findViewById(R.id.btnResetPassword)
        progressBar = findViewById(R.id.progressBar)
        feedbackText = findViewById(R.id.tvSnackbar)

        resetButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val newPassword = newPasswordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()

            feedbackText.text = ""

            if (email.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
                feedbackText.text = "Fields cannot be empty"
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                feedbackText.text = "Invalid email address"
                return@setOnClickListener
            }

            if (newPassword.length < 6) {
                feedbackText.text = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            if (newPassword != confirmPassword) {
                feedbackText.text = "Passwords do not match"
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE
            resetButton.isEnabled = false

            CoroutineScope(Dispatchers.Main).launch {
                val user = withContext(Dispatchers.IO) {
                    repository.getCustomerByEmail(email)
                }

                if (user != null) {
                    withContext(Dispatchers.IO) {
                        repository.updatePassword(email, newPassword)
                    }
                    Toast.makeText(
                        this@ForgotPasswordActivity,
                        "Password Reset Successful!",
                        Toast.LENGTH_LONG
                    ).show()
                    finish() // Navigate back to login or finish
                } else {
                    feedbackText.text = "User not found!"
                }

                progressBar.visibility = View.GONE
                resetButton.isEnabled = true
            }
        }
    }
}
