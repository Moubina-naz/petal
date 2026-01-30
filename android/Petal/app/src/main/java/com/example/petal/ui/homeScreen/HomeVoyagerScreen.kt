import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import com.example.petal.MemoryRepository
import com.example.petal.NavigationEvent
import com.example.petal.Screens.MapVoyagerScreen
import com.example.petal.ui.editMemory.EditMemoryVoyagerScreen
import com.example.petal.ui.homeDetailScreen.MemoryDetailVoyagerScreen
import com.example.petal.ui.homeScreen.HomeScreen
import com.example.petal.ui.homeScreen.HomeViewModel
import com.example.petal.data.remote.ApiProvider


class HomeVoyagerScreen(private val homeViewModel: HomeViewModel) : Screen {
    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val onNavigationEvent: (NavigationEvent) -> Unit = { event ->
            when (event) {
                is NavigationEvent.OpenMemoryDetail -> navigator.push(MemoryDetailVoyagerScreen(event.memory))
                is NavigationEvent.OpenEditMemory -> navigator.push(EditMemoryVoyagerScreen(event.memory))
                NavigationEvent.OpenAddMemory -> navigator.push(EditMemoryVoyagerScreen())
                NavigationEvent.OpenMap -> navigator.push(MapVoyagerScreen()) // Or switch tabs if Map is a separate tab
                NavigationEvent.GoBack -> navigator.pop()
                is NavigationEvent.ToggleFavorite -> homeViewModel.favorite(event.memory)
                else -> {} // Handle others
            }
        }

        HomeScreen(
            viewModel = homeViewModel,
            onNavigationEvent = onNavigationEvent
        )
    }
}