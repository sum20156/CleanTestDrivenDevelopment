package com.example.cleantdd.data.apiservice

import com.example.cleantdd.data.models.UserDataModel
import com.example.cleantdd.data.models.UserResponse
import retrofit2.http.GET

interface ApiService {

    @GET("/users")
    fun getUsers(): UserResponse

    @GET("/blockedUsers")
    fun getBlockedUsers(): List<String>
}