package com.example.petal

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Calendar : Screen("calendar")
    object Map : Screen("map")
    object Profile : Screen("profile")
    object AddMemory : Screen("add_memory")
}