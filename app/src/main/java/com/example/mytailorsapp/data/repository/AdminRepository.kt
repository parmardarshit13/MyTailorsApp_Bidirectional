package com.example.mytailorsapp.data.repository

import com.example.mytailorsapp.data.models.AdminEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AdminRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val adminsCollection = firestore.collection("admins")

    suspend fun loginAdmin(username: String, password: String): AdminEntity? {
        val snapshot = adminsCollection
            .whereEqualTo("username", username)
            .whereEqualTo("password", password)
            .get().await()

        val document = snapshot.documents.firstOrNull()
        return if (document != null) {
            adminsCollection.document(document.id).update("isLoggedIn", true).await()
            document.toObject(AdminEntity::class.java)
        } else {
            null
        }
    }

    suspend fun updateLoginStatus(isLoggedIn: Boolean) {
        val snapshot = adminsCollection
            .whereEqualTo("isLoggedIn", true)
            .get().await()

        snapshot.documents.forEach {
            adminsCollection.document(it.id).update("isLoggedIn", isLoggedIn).await()
        }
    }

    suspend fun getLoggedInAdmin(): AdminEntity? {
        val snapshot = adminsCollection
            .whereEqualTo("isLoggedIn", true)
            .get().await()

        return snapshot.documents.firstOrNull()?.toObject(AdminEntity::class.java)
    }
}
