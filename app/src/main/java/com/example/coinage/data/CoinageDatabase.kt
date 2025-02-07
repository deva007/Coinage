package com.example.coinage.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CoinageAsset::class], version = 1, exportSchema = false)
abstract class CoinageDatabase : RoomDatabase() {
    abstract fun coinageAssetDao(): CoinageAssetDao
}