package com.example.petal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.uniqueScreenKey
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
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PetalTheme {
                var isLoggedIn by remember { mutableStateOf<Boolean?>(null) }
                var splashDone by remember { mutableStateOf(false) }
                var forceLoginKey by remember { mutableStateOf(0) }

                LaunchedEffect(Unit) {
                    // 1. Set unauthorized callback — auto-logout on 401
                    ApiProvider.onUnauthorized = {
                        isLoggedIn = false
                        forceLoginKey++
                    }

                    // 2. Check if token exists
                    isLoggedIn = ApiProvider.authRepository.isLoggedIn()

                    // 3. If logged in, ping Render in background WHILE splash shows
                    //    So by the time user sees HomeScreen, server is already awake
                    if (isLoggedIn == true) {
                        withContext(Dispatchers.IO) {
                            try {
                                ApiProvider.memoryRepository.getMemories(
                                    filter = com.example.petal.ui.homeScreen.HomeFilter.ALL
                                )
                            } catch (e: Exception) {
                                // Ignore — HomeViewModel handles retries
                            }
                        }
                    }
                }

                val ready = splashDone && isLoggedIn != null

                if (!ready) {
                    PetalSplashScreen { splashDone = true }
                } else {
                    key(forceLoginKey) {
                        Navigator(
                            screen = if (isLoggedIn == true)
                                MainAppScreen()
                            else
                                LoginVoyagerScreen()
                        ) {
                            CurrentScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeScaffold() {
    MainTabScreen().Content()
}


class MainAppScreen : Screen {
    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        HomeScaffold()
    }
}

// MainTabScreen.kt
private class MainTabScreen : Screen {
    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val navDepth = remember { mutableStateOf(1) }

        CompositionLocalProvider(LocalTabNavDepth provides navDepth) {
            TabNavigator(JournalTab) {
                Scaffold(
                    bottomBar = {
                        val isTopLevel = navDepth.value == 1
                        AnimatedVisibility(
                            visible = isTopLevel,
                            enter = slideInVertically { it },
                            exit = slideOutVertically { it }
                        ) {
                            PetalBottomNavBar()
                        }
                    }
                ) { padding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        CurrentTab()
                    }
                }
            }
        }
    }
}