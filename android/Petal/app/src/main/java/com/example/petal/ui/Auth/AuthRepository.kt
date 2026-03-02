package com.example.petal.ui.Auth

import com.example.petal.domain.AuthResponse
import com.example.petal.domain.LoginReq
import com.example.petal.domain.RegisterReq
import retrofit2.Response
import java.io.IOException

class AuthRepository(
    private val api: AuthApi,
    private val tokenManager: TokenManager
) {


    suspend fun login(username: String, password: String): Result<Unit> {
        return try {
            val response: Response<AuthResponse> = api.login(LoginReq(username, password))

            if (response.isSuccessful) {
                val authResponse = response.body()
                    ?: return Result.failure(Exception("Empty response body"))

                val access = authResponse.access
                val refresh = authResponse.refresh


                if (access != null && access.isNotBlank() &&
                    refresh != null && refresh.isNotBlank()
                ) {
                    tokenManager.saveTokens(access, refresh, username)
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Tokens missing in response"))
                }
            } else {
                val errorMessage = response.errorBody()?.string()
                    ?.takeIf { it.isNotBlank() }
                    ?: "Login failed (${response.code()})"

                Result.failure(Exception(errorMessage))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}", e))
        } catch (e: Exception) {
            Result.failure(Exception("Unexpected error: ${e.message}", e))
        }
    }

    suspend fun register(
        username: String,
        password: String,
        email: String?
    ): Result<Unit> {
        return try {
            val response: Response<AuthResponse> = api.register(
                RegisterReq(
                    username,
                    password,
                    email
                )
            )

            if (response.isSuccessful) {
                val authResponse = response.body()
                    ?: return Result.failure(Exception("Empty response body"))

                val access = authResponse.access
                val refresh = authResponse.refresh

                if (access != null && access.isNotBlank() &&
                    refresh != null && refresh.isNotBlank()
                ) {
                    tokenManager.saveTokens(access, refresh, username)
                    Result.success(Unit)
                }

                Result.success(Unit)
            } else {
                val errorMessage = response.errorBody()?.string()
                    ?.takeIf { it.isNotBlank() }
                    ?: "Registration failed (${response.code()})"

                Result.failure(Exception(errorMessage))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}", e))
        } catch (e: Exception) {
            Result.failure(Exception("Unexpected error: ${e.message}", e))
        }
    }

    suspend fun logout() {
        tokenManager.clearTokens()   // ← fixed name (was clear() in your version)
    }

    suspend fun isLoggedIn(): Boolean {
        return tokenManager.getAccessToken() != null
    }

    // Optional: helper to get current token (useful for debugging or manual header cases)
    suspend fun getCurrentAccessToken(): String? {
        return tokenManager.getAccessToken()
    }
}