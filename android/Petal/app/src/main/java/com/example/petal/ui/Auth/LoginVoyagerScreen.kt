package com.example.petal.ui.Auth

import HomeVoyagerScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.petal.ui.auth.LoginScreen

class LoginVoyagerScreen : Screen {

    override val key = uniqueScreenKey

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        val viewModel = remember {
            AuthViewModel()
        }

        LoginScreen(
            viewModel = viewModel,
            onLoginSuccess = {
                navigator.replace(HomeVoyagerScreen())
            },
            onSignupClick = {
                navigator.push(SignupVoyagerScreen())
            }
        )
    }
}