package com.example.cleantdd.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cleantdd.ui.theme.CleanTddTheme
import com.example.cleantdd.ui.user_listing.UserListingPageUi
import com.example.cleantdd.viewmodels.UserListingPageViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val userListingPageViewModel:UserListingPageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CleanTddTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(userListingPageViewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(userListingPageViewModel: UserListingPageViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "user_listing") {
        composable("user_listing") { UserListingPageUi(viewModel = userListingPageViewModel) }
    }
}

