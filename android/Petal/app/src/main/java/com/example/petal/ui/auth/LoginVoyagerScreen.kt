package com.example.petal.ui.auth

import com.example.petal.ui.homeScreen.HomeVoyagerScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.petal.MainAppScreen

class LoginVoyagerScreen : Screen {
    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = remember { AuthViewModel() }

        LoginScreen(
            viewModel = viewModel,
            onLoginSuccess = {
                navigator.replaceAll(MainAppScreen())
            },
            onSignupClick = {
                navigator.push(SignUpVoyagerScreen())
            }
        )
    }
}