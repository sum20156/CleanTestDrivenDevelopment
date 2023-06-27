package com.example.cleantdd.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cleantdd.data.models.UserDataModel


@Entity
data class UserEntity(
    @PrimaryKey
    val id:String,
    val name:String?,
    val age:Int?,
    val photoUrl:String?,
    val address:String?,
)

fun List<UserDataModel>.toEntity()=map {
    it.toEntity()
}

fun UserDataModel.toEntity()=
    UserEntity(
        id=id?:throw Exception("user id is null"), //should not use empty string
        name = name,
        age = age,
        photoUrl = photoUrl,
        address = address
    )

