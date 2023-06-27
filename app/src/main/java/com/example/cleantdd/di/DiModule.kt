package com.example.cleantdd.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cleantdd.data.apiservice.ApiService
import com.example.cleantdd.data.db.AppDatabase
import com.example.cleantdd.data.repos.RealUserRepo
import com.example.cleantdd.domain.repos.UserRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DiModule {

    @Provides
    @Singleton
    fun provideRoomDataBase(@ApplicationContext context:Context) =
        Room.databaseBuilder(context,AppDatabase::class.java,"app.db").build()

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase) = appDatabase.userDao()

    @Provides
    @Singleton
    fun provideUserRepo(
        realUserRepo: RealUserRepo
    ):UserRepo = realUserRepo

    @Singleton
    @Provides
    fun provideRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://base_url.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)
}