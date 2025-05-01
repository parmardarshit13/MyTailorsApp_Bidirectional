package com.example.mytailorsapp.data.models

data class MaterialItem(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val image: String = "",
    val category: String = "",
    val status: MaterialStatus
){
    constructor(): this("", "", "", 0.0, "", "", MaterialStatus.AVAILABLE)
}

enum class MaterialStatus {
    AVAILABLE, OUT_OF_STOCK
}