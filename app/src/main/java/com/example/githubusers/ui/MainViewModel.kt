package com.example.githubusers.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubusers.models.list.Users
import com.example.githubusers.models.list.UsersItem
import com.example.githubusers.models.user.User
import com.example.githubusers.repository.Repository
import com.example.githubusers.room.UsersEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val repository: Repository
): ViewModel() {

    val TAG = "UserListViewModel"

    var readUsers: LiveData<UsersEntity> = repository.getLocalData()

    var selectedUser: MutableLiveData<User?> = MutableLiveData()
    var followers: MutableLiveData<Users> = MutableLiveData()
    var following: MutableLiveData<Users> = MutableLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (!repository.checkIfLocalExists()) {
                Log.d(TAG, "no local found")
                getRemoteListData()
            }
        }
    }

    private suspend fun getRemoteListData() {
        try {
            val response = repository.getRemoteData()
            if (response.isSuccessful) {
                repository.insertDataToDb(
                    UsersEntity(users = response.body()!!)
                )
            }
        } catch (e:Exception) { }
    }

    fun selectUser(userLogin: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getUser(userLogin)
                if (response.isSuccessful){
                    selectedUser.postValue(response.body())
                }
            } catch (e:Exception){}
        }
    }

    fun deselectUser() {
        selectedUser.value = null
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