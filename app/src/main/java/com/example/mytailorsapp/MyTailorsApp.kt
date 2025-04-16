package com.example.mytailorsapp

import android.app.Application
import com.google.firebase.FirebaseApp

class MyTailorsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this) // ✅ Initialize Firebase
    }
}
