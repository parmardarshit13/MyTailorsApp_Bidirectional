package com.example.mytailorsapp.data.models

import com.google.firebase.firestore.DocumentId

data class AdminEntity(
    @DocumentId
    val id: String = "",
    val username: String = "",
    val password: String = "",
    val isLoggedIn: Boolean = false
)
