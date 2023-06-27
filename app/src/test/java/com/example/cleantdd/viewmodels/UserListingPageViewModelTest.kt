package com.example.cleantdd.viewmodels

import app.cash.turbine.test
import com.example.MainDispatcherRule
import com.example.cleantdd.data.db.UserEntity
import com.example.cleantdd.data.db.toEntity
import com.example.cleantdd.data.models.UserDataModel
import com.example.cleantdd.data.models.UserResponse
import com.example.cleantdd.data.repos.FakeUserRepo
import com.example.cleantdd.domain.models.toUi
import com.example.cleantdd.domain.usecases.UserListingUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds


class UserListingPageViewModelTest{

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var userListingUseCase: UserListingUseCase
    private lateinit var userRepo: FakeUserRepo
    private lateinit var userListingPageViewModel: UserListingPageViewModel

    @Before
    fun setup(){
        userRepo = FakeUserRepo()
        userListingUseCase = UserListingUseCase(userRepo)
        userListingPageViewModel = UserListingPageViewModel(userRepo,userListingUseCase)
    }

    @Test
    fun `uistate should be loading initially`()= runBlocking{
       val result =  userListingPageViewModel.uiStates.first()

        assertEquals(
            UserListingPageViewModel.UserListingPageUiStates.Loading,
            result)
    }

    @Test
    fun `uistate should be userlisting if there is data in local db`()= runBlocking{
        //add dummy server response
        val userDataList = listOf(
            UserEntity(
                id = UUID.randomUUID().toString(),
                name = "Name",
                age = 22,
                address = "123 block",
                photoUrl = "url",
            ),
        )
        userRepo.insertUserInLocal(userDataList[0])
        userListingPageViewModel.uiStates.test(timeout = 40.seconds) {
            userListingPageViewModel.fetchUserListing()
            awaitItem() //ignoring first emition, it will be LOADING
            val result =  awaitItem()

            assertTrue(
                result is UserListingPageViewModel.UserListingPageUiStates.UserListing
                )
            assertEquals(userDataList.toUi(), result.data)
        }
    }

    @Test
    fun `uistate should be emptystate if there is not data came from server and no data in local db`()= runBlocking{
        //add dummy server response
        userRepo.setDummyUserResponse(
            UserResponse(
                isSuccess = true,
                cursor = "",
                data = emptyList()
            )
        )

        userListingPageViewModel.uiStates.test(timeout = 40.seconds) {
            userListingPageViewModel.fetchUserListing()
            awaitItem() //ignoring first emition, it will be LOADING
            val result =  awaitItem()

            assertTrue(
                result is UserListingPageViewModel.UserListingPageUiStates.EmptyState
            )
        }
    }

    @Test
    fun `showerror should be true if something goes wrong while fetching data from server and no data in db`()= runBlocking{
        //add dummy server response
        userRepo.setDummyUserResponse(
            UserResponse(
                isSuccess = false, //to throw exception
                cursor = "",
                data = null
            )
        )

        userListingPageViewModel.showError.test(timeout = 40.seconds) {
            userListingPageViewModel.refreshUserListingData()
            awaitItem() //ignoring first emition, it will be false as default value
            val result =  awaitItem()
            assertTrue(
                result
            )
        }
    }



}