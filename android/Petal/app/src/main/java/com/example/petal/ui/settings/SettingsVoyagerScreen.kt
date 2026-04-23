package com.example.petal.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.petal.ui.auth.TokenManager
import androidx.compose.ui.platform.LocalContext
import com.example.petal.ui.auth.LoginVoyagerScreen
import kotlinx.coroutines.launch

class SettingsVoyagerScreen : Screen {
    override val key = "SettingsScreen"

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current
        val tokenManager = remember { TokenManager(context) }

        val rootNavigator = generateSequence(navigator) { it.parent }
            .last()

        SettingsScreen(
            onBack = { navigator.pop() },
            onEditProfile = { navigator.push(EditProfileVoyagerScreen()) },
            onLogOut = {
                kotlinx.coroutines.MainScope().launch {
                    tokenManager.clearTokens()

                    // Use ROOT navigator, not the tab-scoped one
                    rootNavigator.replaceAll(LoginVoyagerScreen())
                }
            }
        )
    }
}