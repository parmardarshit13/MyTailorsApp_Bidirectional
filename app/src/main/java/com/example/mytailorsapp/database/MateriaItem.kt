package com.example.mytailorsapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "material_items")
data class MaterialItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val price: String,
    val image: Int,
    val category: String
)

