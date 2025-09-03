package com.example.android_2425_gent2.data.model

import com.example.android_2425_gent2.data.network.model.UserSurfaceInfoDto

class UserSurface(
    val id: Int,
    val familyName: String
)

fun UserSurfaceInfoDto.toDomain() = UserSurface(
    id = id,
    familyName = familyName
)