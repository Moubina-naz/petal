package com.example.petal.ui.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.petal.data.remote.ApiProvider
import com.example.petal.ui.Auth.TokenManager

class ProfileVoyagerScreen : Screen {

    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current

        val viewModel = remember {
            ProfileViewModel(
                repository = ApiProvider.memoryRepository,
                tokenManager = TokenManager(context)           // ← now properly created
            )
        }

        val state by viewModel.uiState.collectAsState()


        ProfileScreen(
            viewModel = viewModel,
            onBack = { navigator.pop() },
            onSettings = {
                // TODO: later navigate to settings
                // navigator.push(SettingsVoyagerScreen())
            }
        )
    }
}