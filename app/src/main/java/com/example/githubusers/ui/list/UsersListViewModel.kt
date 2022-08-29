package com.example.githubusers.ui.list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubusers.models.Users
import com.example.githubusers.models.UsersItem
import com.example.githubusers.repository.Repository
import com.example.githubusers.room.UsersEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class UsersListViewModel @Inject constructor(
    val repository: Repository
): ViewModel() {

    val TAG = "UserListViewModel"

    var readUsers: LiveData<UsersEntity> = repository.getLocalData()

    var selectedUser: MutableLiveData<UsersItem?> = MutableLiveData()
    var followers: MutableLiveData<Users> = MutableLiveData()
    var following: MutableLiveData<Users> = MutableLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (!repository.checkIfLocalExists()) {
                Log.d(TAG, "no local found")
                getRemoteData()
            }
        }
    }

    private suspend fun getRemoteData() {
        try {
            val response = repository.getRemoteData()
            if (response.isSuccessful) {
                repository.insertDataToDb(
                    UsersEntity(users = response.body()!!)
                )
            }
        } catch (e:Exception) { }
    }

    fun selectUser(usersItem: UsersItem) {
        selectedUser.postValue(usersItem)
        Log.d(TAG, "selected ${selectedUser?.value?.login}")
    }

    fun deselectUser() {
        selectedUser.postValue(null)
    }

    fun getFollowers(getFollowing: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = selectedUser.value?.login?.let { repository.getRemoteData(it, !getFollowing) }
                if (response?.isSuccessful == true) {
                    if (!getFollowing) followers.postValue(response.body())
                    else following.postValue(response.body())
                }
            } catch (e: Exception){ }
        }
    }
}