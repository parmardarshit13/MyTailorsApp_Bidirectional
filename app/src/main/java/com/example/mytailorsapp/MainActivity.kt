package com.example.mytailorsapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mytailorsapp.ui.auth.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔄 Redirect to Login or Splash if needed
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // So user can’t return here via back button
    }
}
