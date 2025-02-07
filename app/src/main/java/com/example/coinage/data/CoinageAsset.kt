package com.example.coinage.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asset")
data class CoinageAsset(
    @PrimaryKey val id: String,
    val name: String,
    val symbol: String,
    val priceUsd: String
)