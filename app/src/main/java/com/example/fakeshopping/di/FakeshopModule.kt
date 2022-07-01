package com.example.fakeshopping.di

import com.example.fakeshopping.data.BASE_URL
import com.example.fakeshopping.data.FakeShopApi
import com.example.fakeshopping.data.repository.ShopApiRepository
import com.example.fakeshopping.data.repository.ShopApiRepositoryImpl
import com.example.fakeshopping.data.repository.TestDataRepo
import com.example.fakeshopping.data.test_data.TestApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FakeshopModule {

    @Provides
    @Singleton
    fun provideMoshi():Moshi{
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(moshi:Moshi):Retrofit{
        return Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(MoshiConverterFactory.create(moshi)).build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit:Retrofit):FakeShopApi{
        return retrofit.create(FakeShopApi::class.java)
    }


    @Provides
    @Singleton
    fun provideshopApiRepository(apiService:FakeShopApi):ShopApiRepository{
        return ShopApiRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideTestDataApiService():TestDataRepo{
        return TestDataRepo()
    }

}