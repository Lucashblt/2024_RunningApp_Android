package com.example.runningapp.Data

data class ActivityData(
    val id: Int,
    val userId: Int,
    var likes: Int = 0,
    val distance: Double,
    val time: Int,
    val date: String,
    val sport: ActivityInfo.Sport,
    val map: Int,
    val message : String,
    val routeData: String? = null
)
