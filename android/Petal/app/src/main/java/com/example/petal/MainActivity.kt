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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.example.petal.Screens.PetalBottomNavBar
import com.example.petal.components.JournalTab
import com.example.petal.theme.PetalTheme
import com.example.petal.ui.Auth.LoginVoyagerScreen
import com.example.petal.ui.Auth.SignupVoyagerScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {


            PetalTheme {
                Navigator(LoginVoyagerScreen()) { navigator ->

                    when (navigator.lastItem) {

                        is LoginVoyagerScreen,
                        is SignupVoyagerScreen -> {
                            CurrentScreen()
                        }

                        else -> {
                            TabNavigator(JournalTab) {
                                val tabNavigator = LocalTabNavigator.current

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
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PetalTheme {
        Greeting("Android")
    }
}