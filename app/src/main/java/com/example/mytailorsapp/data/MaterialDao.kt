package com.example.mytailorsapp.data

import androidx.room.*
import com.example.mytailorsapp.database.MaterialItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MaterialDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(material: MaterialItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(materials: List<MaterialItem>)

    @Update
    suspend fun update(material: MaterialItem)

    @Delete
    suspend fun delete(material: MaterialItem)

    @Query("SELECT * FROM material_items ORDER BY name ASC")
    fun getAllMaterials(): Flow<List<MaterialItem>>

    @Query("SELECT * FROM material_items WHERE id = :id")
    suspend fun getMaterialById(id: Int): MaterialItem?

    @Query("SELECT COUNT(*) FROM material_items")
    suspend fun getMaterialCount(): Int

    @Query("DELETE FROM material_items")
    suspend fun deleteAll()
}