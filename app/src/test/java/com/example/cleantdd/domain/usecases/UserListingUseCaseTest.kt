package com.example.cleantdd.domain.usecases

import app.cash.turbine.test
import com.example.MainDispatcherRule
import com.example.cleantdd.data.db.UserEntity
import com.example.cleantdd.data.db.toEntity
import com.example.cleantdd.data.models.UserDataModel
import com.example.cleantdd.data.models.UserResponse
import com.example.cleantdd.data.repos.FakeUserRepo
import com.example.cleantdd.domain.repos.UserRepo
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class UserListingUseCaseTest{




    private lateinit var userListingUseCase: UserListingUseCase
    private lateinit var userRepo: FakeUserRepo

    @Before
    fun setup(){
        userRepo = FakeUserRepo()
        userListingUseCase = UserListingUseCase(userRepo)
    }

    @Test
    fun `userlistingusecase data should not include blocked user`()= runBlocking{
        //add dummy server response
        val userDataList = listOf(
            UserDataModel(
                id = UUID.randomUUID().toString(),
                name = "Name",
                age = 22,
                address = "123 block",
                photoUrl = "url",
                createdAt = -1,
                modifiedAt = -1
            ),
            UserDataModel(
                id = UUID.randomUUID().toString(),
                name = "Name2",
                age = 21,
                address = "1ewde block",
                photoUrl = "urlwe",
                createdAt = -1,
                modifiedAt = -1
            )
        )

        userDataList.toEntity().forEach {
            userRepo.insertUserInLocal(it)
        }
        val blockedUserId=userDataList[1].id!!
        userRepo.setDummyBlockedUser(listOf(blockedUserId))

        userListingUseCase().test {
            val result =awaitItem() //wait for flow emition
            println("result:"+result)
            cancelAndIgnoreRemainingEvents()
            assertTrue(result?.find { it.id== blockedUserId}==null)
        }
    }



    @Test
    fun `userlistingusecase should emit null if no successful response`()= runBlocking{
        //add dummy server response


        userRepo.setDummyBlockedUser(null)

        userListingUseCase().test {
            val result =awaitItem() //wait for flow emition
            cancelAndIgnoreRemainingEvents()
            //emited item should be null, as  as response is not successful
            assertEquals(null,result)

        }

    }


}