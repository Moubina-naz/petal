package com.example.petal.ui.homeScreen

enum class HomeFilter (
    val tag: String?=null,
    val mood: Int?=null,
    val isFavorite: Boolean?=null
    ){
    ALL,
    FAVORITES(isFavorite = true),
    PHOTOS,
    REFLECTIONS(tag = "reflection")

}