package com.example.petal.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.petal.data.remote.ApiProvider

class EditProfileVoyagerScreen : Screen {
    override val key = "EditProfileScreen"

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = remember { EditProfileViewModel(ApiProvider.memoryRepository) }

        EditProfileScreen(
            viewModel = viewModel,
            onBack = { navigator.pop() }
        )
    }
}