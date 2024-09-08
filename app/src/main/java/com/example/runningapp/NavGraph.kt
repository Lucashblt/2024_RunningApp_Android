package com.example.runningapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun Nav(navController: NavHostController, userId: MutableState<Int>) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            Login(navController, userId)
        }
        composable("register") {
            Register(navController, userId)
        }
        composable(
            route = "home/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val currentUserId = backStackEntry.arguments?.getInt("userId") ?: -1
            HomeContent(currentUserId, navController)
        }
        composable(
            route = "profile/{userId}/{profileUserId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
                navArgument("profileUserId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val currentUserId = backStackEntry.arguments?.getInt("userId") ?: -1
            val profileUserId = backStackEntry.arguments?.getInt("profileUserId") ?: -1
            Profile(currentUserId, profileUserId, navController)
        }
        composable(
            route = "activity/{userId}/{activityId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
                navArgument("activityId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val currentUserId = backStackEntry.arguments?.getInt("userId") ?: -1
            val activityId = backStackEntry.arguments?.getInt("activityId") ?: -1
            ActivityCardWithComment(currentUserId, activityId, navController)
        }
        composable(
            route = "maps/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val currentUserId = backStackEntry.arguments?.getInt("userId") ?: -1
            MapPage(currentUserId, navController)
        }
    }
}
