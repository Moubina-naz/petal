package com.example.petal.domain

data class User(
    val id: Int = 0,
    val email: String = "",
    val username: String = "",
    val first_name: String = "",
    val last_name: String = "",
    val base_currency: String = "USD"
)

data class UserRegistration(
    val email: String,
    val username: String,
    val first_name: String,
    val last_name: String,
    val password: String
)

data class PasswordChange(
    val old_password: String,
    val new_password: String
)
data class RegisterReq(
    val username: String,
    val password: String,
    val email: String?
)

data class LoginReq(
    val username: String,
    val password: String
)

data class AuthResponse(
    val access: String?,
    val refresh: String?,
    val message: String?
)