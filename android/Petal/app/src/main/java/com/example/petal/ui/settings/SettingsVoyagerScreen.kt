package com.example.petal.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.petal.data.remote.ApiProvider
import com.example.petal.ui.Auth.TokenManager
import androidx.compose.ui.platform.LocalContext
import com.example.petal.ui.Auth.LoginVoyagerScreen
import kotlinx.coroutines.launch

class SettingsVoyagerScreen : Screen {
    override val key = "SettingsScreen"

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current
        val tokenManager = remember { TokenManager(context) }

        SettingsScreen(
            onBack = { navigator.pop() },
            onEditProfile = { navigator.push(EditProfileVoyagerScreen()) },
            onLogOut = {
                kotlinx.coroutines.MainScope().launch {
                    tokenManager.clearTokens()
                    navigator.popUntilRoot()
                    navigator.replace(LoginVoyagerScreen())
                }
            }
        )
    }
}