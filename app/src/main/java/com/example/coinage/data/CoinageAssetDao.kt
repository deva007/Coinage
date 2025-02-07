package com.example.coinage.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CoinageAssetDao {
    @Query("SELECT * FROM asset")
    suspend fun getAllAssets(): List<CoinageAsset>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(assets: List<CoinageAsset>)

}