package com.example.cleantdd.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cleantdd.data.db.UserEntity
import com.example.cleantdd.data.models.UserDataModel

data class UserListingItemUi(
    val id:String,
    val name:String,
    val photoUrl:String,
)

//should contain ui logic
fun List<UserEntity>.toUi()=map {
    UserListingItemUi(
        id=it.id,
        name = "Mr."+it.name,
        photoUrl = it.photoUrl?:"placeholder link",
    )
}
