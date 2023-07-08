package com.example.cleantdd.domain.usecases

import com.example.cleantdd.data.db.UserEntity
import com.example.cleantdd.data.db.toEntity
import com.example.cleantdd.domain.repos.UserRepo
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class UserListingUseCase @Inject constructor(
    private val userRepo: UserRepo
) {

    operator fun invoke() = flow<List<UserEntity>?> {

        val blockedUsers = userRepo.getBlockedUsers()!!
        coroutineScope {
            userRepo.getUsersFromLocalFlow().collect {
                val userList = it.filter { entity -> (entity.id in blockedUsers).not() }
                emit(userList)
            }
        }



    }.catch {
        it.printStackTrace()
        emit(null)
    }
}