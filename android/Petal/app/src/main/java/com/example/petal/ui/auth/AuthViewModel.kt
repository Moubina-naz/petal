package com.example.petal.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petal.data.remote.ApiProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repo = ApiProvider.authRepository

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            val result = repo.login(username, password)

            _uiState.value = result.fold(
                onSuccess = { AuthUiState.Success },
                onFailure = { AuthUiState.Error(it.message ?: "Login failed") }
            )
        }
    }

    fun register(username: String, password: String, email: String?) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            val result = repo.register(username, password, email)

            _uiState.value = result.fold(
                onSuccess = { AuthUiState.Success },
                onFailure = { AuthUiState.Error(it.message ?: "Register failed") }
            )
        }
    }
    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
    suspend fun isLoggedIn(): Boolean {
        return repo.isLoggedIn()
    }
}