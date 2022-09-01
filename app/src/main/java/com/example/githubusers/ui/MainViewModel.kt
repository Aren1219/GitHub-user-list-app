package com.example.githubusers.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.remember
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
import dagger.hilt.android.qualifiers.ApplicationContext
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
                    getFollowers(true, userLogin)
                    getFollowers(false, userLogin)
                }
            } catch (e:Exception){
//                Toast.makeText(context,"",Toast.LENGTH_LONG).show()
            }
        }
    }

    fun deselectUser() {
        selectedUser.value = null
    }

    private fun getFollowers(getFollowing: Boolean = false, userLogin: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = userLogin.let { repository.getRemoteData(it, !getFollowing) }
                if (response.isSuccessful) {
                    if (!getFollowing) followers.postValue(response.body())
                    else following.postValue(response.body())
                }
            } catch (e: Exception){ }
        }
    }
}