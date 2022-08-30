package com.example.githubusers.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubusers.api.ApiDetails
import com.example.githubusers.api.ApiReferences.BASE_URL
import com.example.githubusers.repository.Repository
import com.example.githubusers.repository.RepositoryImp
import com.example.githubusers.room.UsersDao
import com.example.githubusers.room.UsersDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideHttpInterceptor() =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideApiDetails(okHttpClient: OkHttpClient):ApiDetails {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiDetails::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): UsersDatabase{
        return Room.databaseBuilder(
            context,
            UsersDatabase::class.java,
            "UsersDatabase",
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(usersDatabase: UsersDatabase):UsersDao =
        usersDatabase.getUsersDao()

    @Provides
    @Singleton
    fun provideRep(
        apiDetails: ApiDetails,
        usersDao: UsersDao
    ): Repository = RepositoryImp(apiDetails, usersDao)
}