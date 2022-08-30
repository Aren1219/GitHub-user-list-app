package com.example.githubusers.ui.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.githubusers.models.list.Users
import com.example.githubusers.models.list.UsersItem
import com.example.githubusers.ui.MainViewModel
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun UsersListPage(
    viewModel: MainViewModel
) {
    val data = viewModel.readUsers.observeAsState()
    Column() {
        Surface(
            color = MaterialTheme.colors.primary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "GitHub users list",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(8.dp)
            )
        }
        SearchBar()
        Surface(
            color = MaterialTheme.colors.primaryVariant
        ) {
            data.value?.users?.let {
                UsersList(
                    users = it,
                    selectUser = { user ->  viewModel.selectUser(user)}
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier
) {
    TextField(
        value = "",
        onValueChange = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface
        ),
        placeholder = {
            Text("search user")
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
    )
}

@Composable
fun UsersList(users: Users, selectUser: (UsersItem) -> Unit){
    val listState = rememberLazyListState()
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp),
        state = listState,
    ) {
        items(users){ user ->
            UserItem(usersItem = user, onClick = {selectUser(user)})
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserItem(usersItem: UsersItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.primary,
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            GlideImage(
                imageModel = usersItem.avatarUrl,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(70.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(start = 8.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = usersItem.login,
                    style = MaterialTheme.typography.h5,
                )
                Text(
                    text = usersItem.url,
                )
            }
        }
    }
}

val dummyUser: UsersItem = UsersItem(
    "https://images.unsplash.com/photo-1556302132-40bb13638500?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2952&q=80",
    "",
    "",
    "",
    "",
    "",
    "",
    1,
    "User Name",
    "",
    "",
    "",
    "",
    false,
    "",
    "",
    "",
    "https://api.github.com/users/octocat"
)