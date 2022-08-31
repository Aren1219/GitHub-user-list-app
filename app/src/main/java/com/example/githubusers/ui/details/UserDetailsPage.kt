package com.example.githubusers.ui.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.githubusers.models.list.Users
import com.example.githubusers.models.list.UsersItem
import com.example.githubusers.models.user.User
import com.example.githubusers.ui.MainViewModel
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun UserDetailsPage(
    viewModel: MainViewModel,
) {

    val user = viewModel.selectedUser.observeAsState()
    val followers = viewModel.followers.observeAsState()
    val following = viewModel.following.observeAsState()
    viewModel.getFollowers(false)
    viewModel.getFollowers(true)

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
                            user = it,
                            followers = it1,
                            following = it2,
                            selectUser = { user -> viewModel.selectUser(user) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserDetails(
    user: User,
    followers: Users,
    following: Users,
    selectUser: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            UserNameAndAvatar(user = user)
            UserBio(user = user)
            FollowersList(
                users = followers,
                title = "Followers",
                selectUser = {userLogin -> selectUser(userLogin)}
            )
            FollowersList(
                users = following,
                title = "Following",
                selectUser = {userLogin -> selectUser(userLogin)}
            )
        }
    }
}

@Composable
fun UserNameAndAvatar(user: User){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        GlideImage(
            imageModel = user.avatarUrl,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
        )
        Text(text = user.login, style = MaterialTheme.typography.h3)
        if (user.name != null)
            Text(text = user.name,style = MaterialTheme.typography.h5)
        if (user.location != null)
            Text(text = user.location,style = MaterialTheme.typography.h5)
        if (user.email != null)
            Text(text = user.email, style = MaterialTheme.typography.h5)
    }
}

@Composable
fun FollowerItem(usersItem: UsersItem, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(enabled = true, onClick = onClick),
    ) {
        GlideImage(
            imageModel = usersItem.avatarUrl,
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
        Text(text = usersItem.login)
    }
}

@Composable
fun FollowersList(users: Users, title: String, selectUser: (String) -> Unit) {
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
                    FollowerItem(
                        usersItem = item,
                        onClick = { selectUser(item.login) }
                    )
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

@Composable
fun UserBio(user: User) {
    val paddingValue = 8.dp
    Column() {
        Text(
            text = "Bio",
            modifier = Modifier.padding(paddingValue),
            style = MaterialTheme.typography.h5
        )
        Text(
            text = if (user.bio != null) user.bio else "no bio",
            modifier = Modifier.padding(horizontal = paddingValue)
        )
    }
}
