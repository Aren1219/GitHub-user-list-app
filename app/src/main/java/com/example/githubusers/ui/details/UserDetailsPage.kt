package com.example.githubusers.ui.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import com.example.githubusers.models.Users
import com.example.githubusers.models.UsersItem
import com.example.githubusers.ui.list.UsersListViewModel
import com.skydoves.landscapist.glide.GlideImage
import java.util.*

@Composable
fun UserDetailsPage(
    viewModel: UsersListViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {

    viewModel.getFollowers(false)
    viewModel.getFollowers(true)
    val followers = viewModel.followers.observeAsState()
    val following = viewModel.following.observeAsState()
    val user = viewModel.selectedUser.observeAsState()

    Column() {
        Surface(
            color = MaterialTheme.colors.primary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row() {
                IconButton(onClick = {viewModel.deselectUser()}) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                    )
                }
                Text(
                    text = "GitHub user detail",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Surface(
            color = MaterialTheme.colors.primaryVariant,
            modifier = Modifier.fillMaxHeight()
        ) {
            user.value?.let {
                followers.value?.let { it1 ->
                    following.value?.let { it2 ->
                        UserDetails(
                            usersItem = it,
                            followers = it1,
                            following = it2
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserDetails(usersItem: UsersItem, followers: Users, following: Users) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Column() {
            UserNameAndAvatar(usersItem = usersItem)
            FollowersList(users = followers, "Followers")
            FollowersList(users = following, title = "Following")
        }
    }
}

@Composable
fun UserNameAndAvatar(usersItem: UsersItem){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        GlideImage(
            imageModel = usersItem.avatarUrl,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
        )
        Text(
            text = usersItem.login,
            style = MaterialTheme.typography.h3
        )
    }
}

@Composable
fun FollowerItem(usersItem: UsersItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GlideImage(
            imageModel = usersItem.avatarUrl,
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(text = usersItem.login)
    }
}

@Composable
fun FollowersList(users: Users, title: String) {
    val paddingValue = 8.dp
    Column() {
        Text(
            text = title,
            modifier = Modifier.padding(paddingValue),
            style = MaterialTheme.typography.h5
        )
        if (!users.isEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = paddingValue)
            ) {
                items(users) { item ->
                    FollowerItem(usersItem = item)
                }
            }
        } else {
            Text(
                text = "nobody",
                modifier = Modifier.padding(paddingValue)
            )
        }
    }
}