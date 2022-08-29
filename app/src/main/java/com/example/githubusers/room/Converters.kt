package com.example.githubusers.room

import androidx.room.TypeConverter
import com.example.githubusers.models.Users
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    var gson = Gson()

    @TypeConverter
    fun usersToString(users: Users): String = gson.toJson(users)

    @TypeConverter
    fun stringToUsers(string: String): Users {
        val listType = object : TypeToken<Users>() {}.type
        return gson.fromJson(string, listType)
    }
}