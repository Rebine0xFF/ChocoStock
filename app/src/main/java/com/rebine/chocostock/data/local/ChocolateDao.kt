package com.rebine.chocostock.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ChocolateDao {

    // Sort by expiry date ascending; items without a date placed at the end
    @Query("""
        SELECT * FROM chocolates
        ORDER BY CASE WHEN expiryDateIso IS NULL THEN 1 ELSE 0 END, expiryDateIso ASC
    """)
    fun getAllSortedByExpiry(): Flow<List<ChocolateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chocolate: ChocolateEntity)

    @Delete
    suspend fun delete(chocolate: ChocolateEntity)
}