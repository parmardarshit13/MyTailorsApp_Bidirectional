package com.example.mytailorsapp.data.repository

import android.content.Context
import com.example.mytailorsapp.data.models.CustomerEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CustomerRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val customersCollection = firestore.collection("customers")

    suspend fun registerCustomer(customer: CustomerEntity): Boolean {
        val existing = getCustomerByEmail(customer.email)
        if (existing != null) return false

        val docRef = customersCollection.add(customer).await()
        // If you ever want to return the ID: return docRef.id
        return true
    }


    suspend fun logoutAllCustomers() {
        val snapshot = customersCollection.whereEqualTo("isLoggedIn", true).get().await()
        snapshot.documents.forEach {
            it.reference.update("isLoggedIn", false).await()
        }
    }

    suspend fun updateLoginStatus(email: String, isLoggedIn: Boolean) {
        val snapshot = customersCollection.whereEqualTo("email", email).get().await()
        snapshot.documents.forEach {
            it.reference.update("isLoggedIn", isLoggedIn).await()
        }
    }

    // ✅ Uses @DocumentId to get id field filled
    suspend fun getCustomerById(customerId: String): CustomerEntity? {
        val doc = customersCollection.document(customerId).get().await()
        return if (doc.exists()) {
            doc.toObject(CustomerEntity::class.java)
        } else null
    }

    // ✅ Uses @DocumentId with firstOrNull doc
    suspend fun getCustomerByEmail(email: String): CustomerEntity? {
        val snapshot = customersCollection
            .whereEqualTo("email", email)
            .get().await()

        val doc = snapshot.documents.firstOrNull()
        return doc?.toObject(CustomerEntity::class.java)?.copy(id = doc.id)
    }

    // ✅ Updates customer by doc ID
    suspend fun updateCustomerProfile(customer: CustomerEntity) {
        if (customer.id.isNotBlank()) {
            customersCollection.document(customer.id).set(customer).await()
        }
    }

    suspend fun updatePassword(email: String, newPassword: String) {
        val snapshot = customersCollection.whereEqualTo("email", email).get().await()
        snapshot.documents.firstOrNull()?.reference?.update("password", newPassword)?.await()
    }

    // ✅ Uses @DocumentId for login result
    suspend fun login(email: String, password: String): CustomerEntity? {
        val snapshot = customersCollection
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get().await()

        val doc = snapshot.documents.firstOrNull()
        return doc?.toObject(CustomerEntity::class.java)?.copy(id = doc.id)
    }

    suspend fun logout(email: String) {
        val snapshot = customersCollection.whereEqualTo("email", email).get().await()
        snapshot.documents.firstOrNull()?.reference?.update("isLoggedIn", false)?.await()
    }

    fun getLoggedInCustomerId(context: Context): String? {
        val sharedPref = context.getSharedPreferences("MyTailorPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("loggedInCustomerId", null)
    }
}
