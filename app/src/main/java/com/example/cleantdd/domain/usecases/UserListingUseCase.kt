package com.example.cleantdd.domain.usecases

import com.example.cleantdd.data.db.UserEntity
import com.example.cleantdd.data.db.toEntity
import com.example.cleantdd.domain.repos.UserRepo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserListingUseCase @Inject constructor (
    private val userRepo: UserRepo
    ) {

    operator fun invoke()= flow<List<UserEntity>?>{
        try {
            val userResponse = userRepo.getUsersFromServer()
            if (userResponse.isSuccess){
                val dataList = arrayListOf<UserEntity>()
                userResponse.data?.forEach {
                    // business logic
                    if (it.id!=null){
                        val entity =it.toEntity()
                        dataList.add(entity)
                        userRepo.insertUserInLocal(entity)
                    }
                }
                emit(dataList)
            }else{
                throw Exception("User Response is not successful")
            }
        }catch (e:Exception){
            emit(null)
        }

    }
}