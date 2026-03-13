package com.example.petal.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.petal.ui.homeScreen.HomeVoyagerScreen

class SignUpVoyagerScreen : Screen {

    override val key = uniqueScreenKey

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        val viewModel = remember {
            AuthViewModel()
        }

        SignupScreen(
            viewModel = viewModel,
            onSignupSuccess = {
                navigator.replace(HomeVoyagerScreen())
            },
            onBackClick = {
                navigator.pop()
            }
        )
    }
}