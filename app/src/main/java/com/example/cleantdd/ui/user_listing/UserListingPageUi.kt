package com.example.cleantdd.ui.user_listing

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.cleantdd.domain.models.UserListingItemUi
import com.example.cleantdd.viewmodels.UserListingPageViewModel


@Composable
fun UserListingPageUi(
    viewModel: UserListingPageViewModel
) {


    Box(modifier = Modifier.fillMaxSize()) {
        val uiState = viewModel.uiStates.collectAsState()
        val showError =viewModel.showError.collectAsState()
        ErrorState(showError)
        when(val uiStateValue = uiState.value){
            UserListingPageViewModel.UserListingPageUiStates.Loading->{
                viewModel.fetchUserListing()
                viewModel.refreshUserListingData()
                LoadingState()
            }
            UserListingPageViewModel.UserListingPageUiStates.EmptyState->{
                EmptyState()
            }

            is UserListingPageViewModel.UserListingPageUiStates.UserListing->{
                ListingState(uiStateValue.data)
            }

        }
    }

}

@Composable
fun LoadingState() {
    Text(text = "Loading")
}

@Composable
fun EmptyState() {
    Text(text = "Empty data")

}

@Composable
fun ErrorState(showError: State<Boolean>) {
    if (showError.value){
        Toast.makeText(LocalContext.current, "Something went wrong", Toast.LENGTH_SHORT).show()
    }

}

@Composable
fun ListingState(list:List<UserListingItemUi>) {
    LazyColumn(){
        items(list){
            UserColumnItem(userListingItemUi = it)
        }
    }

}

@Composable
fun UserColumnItem(userListingItemUi: UserListingItemUi) {
    Text(text = userListingItemUi.name)
}