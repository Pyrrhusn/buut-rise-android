package com.example.android_2425_gent2.data.network.model

data class ReservationResponse(
    val data: List<ReservationDto>,
    val nextId: Int? = null,
    val previousId: Int? = null,
    val isFirstPage: Boolean = true,
)