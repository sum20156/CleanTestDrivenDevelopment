package com.example.cleantdd.data.models


data class UserResponse(
    val isSuccess:Boolean,
    val cursor:String,
    val data:List<UserDataModel>?
)

data class UserDataModel(
    val id:String?,
    val name:String?,
    val age:Int?,
    val photoUrl:String?,
    val address:String?,
    val createdAt:Long?,
    val modifiedAt:Long?,
)
