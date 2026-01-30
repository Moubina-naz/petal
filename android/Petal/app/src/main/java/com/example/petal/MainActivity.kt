package com.example.petal

import HomeVoyagerScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.example.petal.Screens.PetalBottomNavBar
import com.example.petal.components.JournalTab
import com.example.petal.ui.homeScreen.HomeScreen
import com.example.petal.theme.PetalTheme
import com.example.petal.ui.homeScreen.HomeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetalTheme {
                TabNavigator(JournalTab) { // Start on Journal tab
                    val tabNavigator = LocalTabNavigator.current

                    Scaffold(
                        bottomBar = {
                            PetalBottomNavBar(
                                // No selectedTab or onTabSelected needed anymore!
                                // onAddClick is optional — only pass if you want a middle button in the bar
                                // onAddClick = { /* handle middle add button if you have one */ }
                            )
                        },
                        floatingActionButton = {
                            if (tabNavigator.current == JournalTab) {
                                FloatingActionButton(
                                    onClick = {
                                        // TODO: Open "Add Memory" screen
                                        // Example: tabNavigator.current = EditMemoryVoyagerScreen()  (but better to push on Journal's nested navigator)
                                        // For now, you can use LocalNavigator from inside HomeScreen
                                    },
                                    containerColor = Color(0xFF6B4F3F)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Add Memory")
                                }
                            }
                        },
                        floatingActionButtonPosition = FabPosition.End // or Center if you want it centered
                    ) { innerPadding ->
                        // This renders the current tab's content (with nested navigator for Journal)
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