package com.example.petal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.example.petal.Screens.PetalBottomNavBar
import com.example.petal.components.JournalTab
import com.example.petal.data.remote.ApiProvider
import com.example.petal.theme.PetalTheme
import com.example.petal.ui.auth.LoginVoyagerScreen
import com.example.petal.ui.auth.SignUpVoyagerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PetalTheme {

                // null = still checking, true = logged in, false = not logged in
                var isLoggedIn by remember { mutableStateOf<Boolean?>(null) }
                var splashDone by remember { mutableStateOf(false) }

                // Check session once on launch (fast DataStore read)
                LaunchedEffect(Unit) {
                    isLoggedIn = ApiProvider.authRepository.isLoggedIn()
                }

                // Wait for BOTH splash animation AND session check before moving on
                val ready = splashDone && isLoggedIn != null

                if (!ready) {
                    // Always show splash while either is still pending
                    PetalSplashScreen(
                        onFinished = { splashDone = true }
                    )
                } else if (isLoggedIn == true) {
                    // Already logged in → go straight to home
                    HomeScaffold()
                } else {
                    // Not logged in → normal login flow
                    Navigator(LoginVoyagerScreen()) { navigator ->
                        when (navigator.lastItem) {
                            is LoginVoyagerScreen,
                            is SignUpVoyagerScreen -> CurrentScreen()
                            else -> {
                                val sessionKey = remember(navigator.lastItem) {
                                    System.currentTimeMillis()
                                }
                                key(sessionKey) {
                                    HomeScaffold()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeScaffold() {
    TabNavigator(JournalTab) {
        Scaffold(
            bottomBar = { PetalBottomNavBar() },
            floatingActionButton = {},
            floatingActionButtonPosition = FabPosition.End
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                CurrentTab()
            }
        }
    }
}


