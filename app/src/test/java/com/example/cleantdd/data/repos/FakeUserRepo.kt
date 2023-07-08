package com.example.cleantdd.data.repos

import com.example.cleantdd.data.db.UserEntity
import com.example.cleantdd.data.db.toEntity
import com.example.cleantdd.data.models.UserResponse
import com.example.cleantdd.domain.repos.UserRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeUserRepo:UserRepo{

    private lateinit var dummyUserResponse: UserResponse
    private var blockedUserResponse: List<String>?= emptyList()


    private val userEntityList:ArrayList<UserEntity> = arrayListOf()

    fun setDummyUserResponse(response: UserResponse){
        this.dummyUserResponse = response
    }

    fun setDummyBlockedUser(response: List<String>?){
        this.blockedUserResponse = response
    }
    override suspend fun getUsersFromServer(): UserResponse {
        return dummyUserResponse
    }

    override suspend fun getUsersFromLocal(): List<UserEntity> {
        return userEntityList
    }

    override fun getUsersFromLocalFlow()= flow<List<UserEntity>> {
       emit(userEntityList)
        //background workd using dummy server response
        //emit to entity

    }



    override fun getBlockedUsers(): List<String>? {
        return blockedUserResponse
    }

    override suspend fun insertUserInLocal(userEntity: UserEntity) {
        userEntityList.add(userEntity)
    }

}