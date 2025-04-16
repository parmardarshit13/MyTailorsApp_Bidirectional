package com.example.mytailorsapp.data.repository

import android.content.Context
import android.net.Uri
import com.example.mytailorsapp.data.models.CustomerEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class CustomerRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val customersCollection = firestore.collection("customers")

    suspend fun registerCustomer(customer: CustomerEntity): Boolean {
        val existing = getCustomerByEmail(customer.email)
        if (existing != null) return false

        customersCollection.add(customer).await()
        return true
    }

    suspend fun authenticate(email: String, password: String): CustomerEntity? {
        val snapshot = customersCollection
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get().await()

        return snapshot.documents.firstOrNull()?.toObject(CustomerEntity::class.java)
    }

    suspend fun updateLoginStatus(email: String, isLoggedIn: Boolean) {
        val customer = getCustomerByEmail(email)
        if (customer != null) {
            customersCollection
                .whereEqualTo("email", email)
                .get().await()
                .documents.firstOrNull()?.id?.let { docId ->
                    customersCollection.document(docId).update("isLoggedIn", isLoggedIn).await()
                }
        }
    }

    suspend fun getCustomerById(customerId: Int): CustomerEntity? {
        val snapshot = customersCollection
            .whereEqualTo("id", customerId)
            .get().await()

        return snapshot.documents.firstOrNull()?.toObject(CustomerEntity::class.java)
    }

    suspend fun getCustomerByEmail(email: String): CustomerEntity? {
        val snapshot = customersCollection
            .whereEqualTo("email", email)
            .get().await()

        return snapshot.documents.firstOrNull()?.toObject(CustomerEntity::class.java)
    }

    suspend fun updateCustomerProfile(customer: CustomerEntity) {
        customersCollection
            .whereEqualTo("id", customer.id)
            .get().await()
            .documents.firstOrNull()?.id?.let { docId ->
                customersCollection.document(docId).set(customer).await()
            }
    }

    suspend fun updatePassword(email: String, newPassword: String) {
        customersCollection
            .whereEqualTo("email", email)
            .get().await()
            .documents.firstOrNull()?.id?.let { docId ->
                customersCollection.document(docId).update("password", newPassword).await()
            }
    }

    suspend fun login(email: String, password: String): CustomerEntity? {
        val snapshot = customersCollection
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get()
            .await()
        return snapshot.documents.firstOrNull()?.toObject(CustomerEntity::class.java)
    }

    suspend fun logout(email: String) {
        val snapshot = customersCollection
            .whereEqualTo("email", email)
            .get()
            .await()
        snapshot.documents.firstOrNull()?.id?.let { docId ->
            customersCollection.document(docId).update("isLoggedIn", false).await()
        }
    }

    suspend fun getLoggedInCustomerId(): Int? {
        val snapshot = customersCollection
            .whereEqualTo("isLoggedIn", true)
            .get()
            .await()
        return snapshot.documents.firstOrNull()?.toObject(CustomerEntity::class.java)?.id
    }

//    suspend fun logoutAllCustomers() {
//        val snapshot = customersCollection
//            .whereEqualTo("isLoggedIn", true)
//            .get()
//            .await()
//        snapshot.documents.forEach {
//            customersCollection.document(it.id).update("isLoggedIn", false).await()
//        }
//    }

    suspend fun uploadProfileImage(customerId: Int, imageUri: Uri, context: Context): String? {
        val storageRef = FirebaseStorage.getInstance().reference
            .child("profile_images/customer_$customerId.jpg")

        val uploadTask = storageRef.putFile(imageUri).await()
        return storageRef.downloadUrl.await().toString()
    }

    suspend fun updateProfileImageUrl(customerId: Int, url: String) {
        val snapshot = customersCollection
            .whereEqualTo("id", customerId)
            .get().await()

        snapshot.documents.firstOrNull()?.reference?.update("profileImageUrl", url)?.await()
    }
}
