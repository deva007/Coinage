package com.example.coinage.di

import android.content.Context
import androidx.room.Room
import com.example.coinage.data.CoinageDatabase
import com.example.coinage.data.CoinageAssetDao
import com.example.coinage.data.CoinageService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideCoinageService(retrofit: Retrofit): CoinageService {
        return retrofit.create(CoinageService::class.java)
    }

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.coincap.io/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideCoinageAssetDao(database: CoinageDatabase): CoinageAssetDao {
        return database.coinageAssetDao()
    }


    @Provides
    fun provideCoinageDatabase(@ApplicationContext context: Context): CoinageDatabase {
        return Room.databaseBuilder(
            context,
            CoinageDatabase::class.java,
            "coinage_database"
        ).build()
    }
}