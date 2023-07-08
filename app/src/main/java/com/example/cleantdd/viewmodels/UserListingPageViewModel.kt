package com.example.cleantdd.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.cleantdd.domain.models.UserListingItemUi
import com.example.cleantdd.domain.models.toUi
import com.example.cleantdd.domain.repos.UserRepo
import com.example.cleantdd.domain.usecases.UserListingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListingPageViewModel @Inject constructor(
    private val userListingUseCase: UserListingUseCase
) : ViewModel() {


    private val _uiStates: MutableStateFlow<UserListingPageUiStates> =
        MutableStateFlow(UserListingPageUiStates.Loading)
    val uiStates: StateFlow<UserListingPageUiStates> = _uiStates

    private val _showError: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val showError: StateFlow<Boolean> = _showError


    sealed interface UserListingPageUiStates {
        object Loading : UserListingPageUiStates
        object EmptyState : UserListingPageUiStates
        class UserListing(val data: List<UserListingItemUi>) : UserListingPageUiStates
    }


/*    fun fetchUserListing() {

        userRepo.getUsersFromLocalFlow().onEach { localData ->
            if (localData.isEmpty().not()) {
                _uiStates.value = UserListingPageUiStates.UserListing(localData.toUi())
            } else {
                _uiStates.value = UserListingPageUiStates.EmptyState
            }

        }.launchIn(viewModelScope)


    }*/

    fun refreshUserListingData() {
        userListingUseCase().onEach {
            when {
                it == null -> {
                    _showError.value = true
                }
                it.isEmpty()-> {
                    _uiStates.value = UserListingPageUiStates.EmptyState
                }
                else -> {
                    _uiStates.value = UserListingPageUiStates.UserListing(it.toUi())
                }

            }
        }.launchIn(viewModelScope)

    }


}