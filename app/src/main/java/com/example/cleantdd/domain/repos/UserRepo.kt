package com.example.cleantdd.domain.repos

import com.example.cleantdd.data.db.UserEntity
import com.example.cleantdd.data.models.UserDataModel
import com.example.cleantdd.data.models.UserResponse
import kotlinx.coroutines.flow.Flow

interface UserRepo {

    suspend fun getUsersFromServer():UserResponse

    suspend fun getUsersFromLocal():List<UserEntity>

    fun getUsersFromLocalFlow():Flow<List<UserEntity>>


    suspend fun insertUserInLocal(userEntity: UserEntity)
}