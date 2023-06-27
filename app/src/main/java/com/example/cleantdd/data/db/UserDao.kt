package com.example.cleantdd.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)



    @Query("SELECT * FROM UserEntity")
    suspend fun getAllUsers(): List<UserEntity>


    @Query("SELECT * FROM UserEntity")
    fun getAllUsersFlow(): Flow<List<UserEntity>>

}