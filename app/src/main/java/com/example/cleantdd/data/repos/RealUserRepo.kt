package com.example.cleantdd.data.repos

import com.example.cleantdd.data.apiservice.ApiService
import com.example.cleantdd.data.db.AppDatabase
import com.example.cleantdd.data.db.UserDao
import com.example.cleantdd.data.db.UserEntity
import com.example.cleantdd.data.db.toEntity
import com.example.cleantdd.data.models.UserDataModel
import com.example.cleantdd.data.models.UserResponse
import com.example.cleantdd.domain.repos.UserRepo
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class RealUserRepo @Inject constructor(
    private val userDao: UserDao,
    private val apiService: ApiService
):UserRepo {

    // this is for demo purpose only, on real world scenario it should come from server
    val realResponse  = UserResponse(
        isSuccess = true,
        cursor = "",
        data = listOf(
            UserDataModel(
                id = "test123",
                name = "John Ram",
                age = 22,
                address = "234 vloam, India",
                createdAt = -1,
                modifiedAt = -1,
                photoUrl = "www.sds.com"
            ),
            UserDataModel(
                id = "test546",
                name = "Akash",
                age = 54,
                address = "sds4 vloam, India",
                createdAt = -1,
                modifiedAt = -1,
                photoUrl = "www.sadas.com"
            )
        )
    )
    override suspend fun getUsersFromServer(): UserResponse {
       //return apiService.getUsers() //real scenario
        return realResponse
    }

    override suspend fun getUsersFromLocal(): List<UserEntity> {
       return userDao.getAllUsers()
    }

    override fun getUsersFromLocalFlow() = userDao.getAllUsersFlow().combine(refreshFromRemote()){list,p->
        list
    }

    private fun refreshFromRemote()= flow<Unit> {
        val userResponse = getUsersFromServer()
        if (userResponse.isSuccess){
            userResponse.data?.forEach {
                if (it.id!=null){
                    val entity =it.toEntity()
                    insertUserInLocal(entity)
                }
            }
            emit(Unit)
        }else{
            throw Exception("User Response is not successful")
        }
    }

    override fun getBlockedUsers(): List<String>? {
        //return apiService.getBlockedUsers() //real scenario
        return listOf(realResponse.data?.get(1)?.id!!)
    }

    override suspend fun insertUserInLocal(userEntity: UserEntity) {
        userDao.insert(userEntity)
    }
}