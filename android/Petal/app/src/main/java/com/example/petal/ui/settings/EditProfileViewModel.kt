package com.example.petal.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petal.MemoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class EditProfileUiState(
    val username: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val oldPassword: String = "",
    val newPassword: String = "",
    val isSavingProfile: Boolean = false,
    val isSavingPassword: Boolean = false,
    val profileError: String? = null,
    val profileSuccess: String? = null,
    val passwordError: String? = null,
    val passwordSuccess: String? = null
)

class EditProfileViewModel(
    private val repository: MemoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState

    init { loadProfile() }

    private fun loadProfile() {
        viewModelScope.launch {
            try {
                val profile = repository.getProfile()
                _uiState.value = _uiState.value.copy(
                    username = profile.username,
                    email = profile.email,
                    firstName = profile.firstName,
                    lastName = profile.lastName
                )
            } catch (e: Exception) {}
        }
    }

    fun onUsernameChange(v: String) = _uiState.value.let { _uiState.value = it.copy(username = v) }
    fun onEmailChange(v: String) = _uiState.value.let { _uiState.value = it.copy(email = v) }
    fun onFirstNameChange(v: String) = _uiState.value.let { _uiState.value = it.copy(firstName = v) }
    fun onLastNameChange(v: String) = _uiState.value.let { _uiState.value = it.copy(lastName = v) }
    fun onOldPasswordChange(v: String) = _uiState.value.let { _uiState.value = it.copy(oldPassword = v) }
    fun onNewPasswordChange(v: String) = _uiState.value.let { _uiState.value = it.copy(newPassword = v) }

    fun saveProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSavingProfile = true, profileError = null, profileSuccess = null)
            try {
                val s = _uiState.value
                repository.updateProfile(s.username, s.email, s.firstName, s.lastName)
                _uiState.value = _uiState.value.copy(isSavingProfile = false, profileSuccess = "Profile updated!")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isSavingProfile = false, profileError = e.message ?: "Failed to update")
            }
        }
    }

    fun changePassword() {
        val s = _uiState.value
        if (s.oldPassword.isBlank() || s.newPassword.isBlank()) {
            _uiState.value = s.copy(passwordError = "Both fields are required")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSavingPassword = true, passwordError = null, passwordSuccess = null)
            try {
                repository.changePassword(s.oldPassword, s.newPassword)
                _uiState.value = _uiState.value.copy(
                    isSavingPassword = false,
                    passwordSuccess = "Password changed!",
                    oldPassword = "",
                    newPassword = ""
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isSavingPassword = false, passwordError = "Wrong current password")
            }
        }
    }
}