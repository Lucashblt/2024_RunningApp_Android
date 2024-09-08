package com.example.runningapp.Data

import android.annotation.SuppressLint
import com.example.runningapp.R

object ActivityInfo {
    enum class Sport(val id: Int, val displayName: String, val icon: Int) {
        Running(1, "Running", R.drawable.running),
        Cycling(2, "Cycling", R.drawable.cycling),
        Swimming(3, "Swimming", R.drawable.swimming)
    }

    private val activities: MutableList<ActivityData> = mutableListOf(
        ActivityData(1, 1, 0, 10.0, 56 * 60, "2021-01-01", Sport.Running, R.drawable.tracer, "Good run!"),
        ActivityData(2, 1, 90, 20.0, 45 * 60, "2021-01-20", Sport.Cycling, R.drawable.tracer, "Nice ride!"),
        ActivityData(3, 1, 6, 40.0, 80 * 60, "2021-01-03", Sport.Swimming, R.drawable.tracer, "Long ride!"),
        ActivityData(4, 1, 0, 12.0, 1205, "2021-01-28", Sport.Running, R.drawable.tracer, "Great run!"),
        ActivityData(5, 1, 0, 15.0, 90 * 60, "2021-01-05", Sport.Running, R.drawable.tracer, "Intense run!"),

        ActivityData(6, 2, 0, 5.0, 30 * 60, "2021-01-06", Sport.Running, R.drawable.tracer, "Quick run!"),
        ActivityData(7, 2, 60, 25.0, 60 * 60, "2021-01-15", Sport.Cycling, R.drawable.tracer, "Great ride!"),
        ActivityData(8, 2, 0, 10.0, 60 * 60, "2021-01-08", Sport.Running, R.drawable.tracer, "Good run!"),
        ActivityData(9, 2, 90, 30.0, 90 * 60, "2021-01-16", Sport.Cycling, R.drawable.tracer, "Awesome ride!"),
        ActivityData(10, 2, 0, 8.0, 50 * 60, "2021-01-10", Sport.Swimming, R.drawable.tracer, "Nice run!"),

        ActivityData(11, 3, 0, 7.0, 40 * 60, "2021-01-07", Sport.Running, R.drawable.tracer, "Quick run!"),
        ActivityData(12, 3, 90, 22.0, 70 * 60, "2021-01-12", Sport.Cycling, R.drawable.tracer, "Good ride!"),
        ActivityData(13, 3, 0, 9.0, 55 * 60, "2021-01-19", Sport.Running, R.drawable.tracer, "Steady run!"),
        ActivityData(14, 3, 120, 35.0, 100 * 60, "2021-01-14", Sport.Cycling, R.drawable.tracer, "Long ride!"),
        ActivityData(15, 3, 0, 11.0, 65 * 60, "2021-01-27", Sport.Running, R.drawable.tracer, "Great run!"),

        ActivityData(16, 4, 0, 4.0, 25 * 60, "2021-01-13", Sport.Running, R.drawable.tracer, "Short run!"),
        ActivityData(17, 4, 80, 18.0, 60 * 60, "2021-01-19", Sport.Cycling, R.drawable.tracer, "Good ride!"),
        ActivityData(18, 4, 0, 7.0, 45 * 60, "2021-01-15", Sport.Running, R.drawable.tracer, "Nice run!"),
        ActivityData(19, 4, 100, 28.0, 85 * 60, "2021-01-05", Sport.Cycling, R.drawable.tracer, "Awesome ride!"),
        ActivityData(20, 4, 0, 9.0, 55 * 60, "2021-01-20", Sport.Running, R.drawable.tracer, "Good run!"),

        ActivityData(21, 5, 0, 6.0, 35 * 60, "2021-01-04", Sport.Running, R.drawable.tracer, "Quick run!"),
        ActivityData(22, 5, 75, 20.0, 65 * 60, "2021-01-02", Sport.Swimming, R.drawable.tracer, "Nice ride!"),
        ActivityData(23, 5, 0, 8.0, 50 * 60, "2021-01-13", Sport.Running, R.drawable.tracer, "Steady run!"),
        ActivityData(24, 5, 110, 32.0, 95 * 60, "2021-01-20", Sport.Swimming, R.drawable.tracer, "Great ride!"),
        ActivityData(25, 5, 0, 10.0, 60 * 60, "2021-01-26", Sport.Running, R.drawable.tracer, "Good run!")
    )

    fun getActivitiesByUserId(userId: Int): List<ActivityData> {
        return activities.filter { it.userId == userId }.sortedByDescending { it.date }
    }

    fun getActivities(): List<ActivityData> {
        activities.sortByDescending { it.date }
        return activities
    }
    fun getActivityById(id: Int): ActivityData? {
        return activities.find { it.id == id }
    }

    @SuppressLint("DefaultLocale")
    fun calculatePace(distance: Double, timeInSeconds: Int): String {
        if (distance == 0.0 || timeInSeconds == 0) {
            return "0:00 min/km"
        }else{
            val tempsEnMinutes = timeInSeconds.toDouble() / 60.0
            val paceEnMinParKm = tempsEnMinutes / distance

            val paceMinutes = paceEnMinParKm.toInt()
            val paceSeconds = ((paceEnMinParKm - paceMinutes) * 60).toInt()
            val formattedSeconds = String.format("%02d", paceSeconds)
            return "$paceMinutes:$formattedSeconds min/km"
        }
    }

    fun add(activity: ActivityData) {
        activities.add(activity.copy(likes = 0))
    }
}