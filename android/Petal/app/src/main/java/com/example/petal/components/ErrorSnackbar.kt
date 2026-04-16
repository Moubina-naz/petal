package com.example.petal.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Composable
fun ErrorSnackbar(
    message: String,
    onDismiss: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(message) {
        snackbarHostState.showSnackbar(
            message  = message,
            duration = SnackbarDuration.Short
        )
        onDismiss()
    }

    SnackbarHost(hostState = snackbarHostState) { data ->
        Snackbar(
            snackbarData    = data,
            containerColor  = MaterialTheme.colorScheme.primary,
            contentColor    = MaterialTheme.colorScheme.surface,
        )
    }
}