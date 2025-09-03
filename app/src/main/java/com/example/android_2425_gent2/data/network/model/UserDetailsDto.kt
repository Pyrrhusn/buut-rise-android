package com.example.android_2425_gent2.data.network.model

data class UserDetailsDto(
    val firstName: String,
    val email: String,
    val phoneNumber: String,
    val address: AddressDto,
    val familyName: String,
    val id: Int
)