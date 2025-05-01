package com.example.mytailorsapp.data.models

data class CartItem(
    val id: String = "",                  // Firebase document ID
    val customerId: String = "",
    val name: String = "",
    val category: String = "",
    val pricePerMeter: Double = 0.0,
    val imageUrl: String = "",
    val meters: Int = 1
) {
    val totalPrice: Double
        get() = pricePerMeter * meters
}