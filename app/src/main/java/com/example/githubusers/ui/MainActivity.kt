package com.example.githubusers.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.githubusers.ui.details.UserDetailsPage
import com.example.githubusers.ui.list.UsersListPage
import com.example.githubusers.ui.list.UsersListViewModel
import com.example.githubusers.ui.theme.GitHubUsersTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GitHubUsersTheme {
                // A surface container using the 'background' color from the theme
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: UsersListViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        val user = viewModel.selectedUser.observeAsState()

        if (user.value == null)
            UsersListPage()
        else
            UserDetailsPage()
    }
}