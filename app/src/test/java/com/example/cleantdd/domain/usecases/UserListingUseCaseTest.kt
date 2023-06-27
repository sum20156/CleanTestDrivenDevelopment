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


class UserListingUseCaseTest{




    private lateinit var userListingUseCase: UserListingUseCase
    private lateinit var userRepo: FakeUserRepo

    @Before
    fun setup(){
        userRepo = FakeUserRepo()
        userListingUseCase = UserListingUseCase(userRepo)
    }

    @Test
    fun `userlistingusecase should add data into local_db coming from server`()= runBlocking{
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
        userRepo.setDummyUserResponse(
            UserResponse(
                isSuccess = true,
                cursor = "",
                data = userDataList
            )
        )

        userListingUseCase().test {
            val result =awaitItem() //wait for flow emition
            cancelAndIgnoreRemainingEvents()
            //comparing emited item is same as response data, this ensure response to entity mapping is working fine
            assertEquals(userDataList.toEntity(),result)
            //checking all data inserted into db, coming from response
            userDataList.forEach {
                assertEquals(
                    it.toEntity(),userRepo.getUsersFromLocal().find { userEntity -> userEntity.id==it.id }
                )
            }

        }
    }

    @Test
    fun `userlistingusecase should not add data into local_db if id is null of user data coming from server`()= runBlocking{
        //add dummy server response
        val userDataList = listOf(
            UserDataModel(
                id = null, //making id field null
                name = "Name",
                age = 22,
                address = "123 block",
                photoUrl = "url",
                createdAt = -1,
                modifiedAt = -1
            )
        )
        userRepo.setDummyUserResponse(
            UserResponse(
                isSuccess = true,
                cursor = "",
                data = userDataList
            )
        )

        userListingUseCase().test {
            val result =awaitItem() //wait for flow emition
            cancelAndIgnoreRemainingEvents()
            //emited item should be empty list, as we have only one data in response and the id is null
            assertEquals(emptyList(),result)
            //local db should db empty, as if id is null data should not be inserted
            assertEquals(emptyList(),userRepo.getUsersFromLocal())

        }
    }

    @Test
    fun `userlistingusecase should emit null if no successful response`()= runBlocking{
        //add dummy server response
        userRepo.setDummyUserResponse(
            UserResponse(
                isSuccess = false,
                cursor = "",
                data = null
            )
        )

        userListingUseCase().test {
            val result =awaitItem() //wait for flow emition
            cancelAndIgnoreRemainingEvents()
            //emited item should be null, as  as response is not successful
            assertEquals(null,result)
            //local db should db empty, as response is not successful
            assertEquals(emptyList(),userRepo.getUsersFromLocal())

        }
    }


}