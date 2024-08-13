package app.skypub.notification

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import app.skypub.common.ProfileUiState
import app.skypub.common.ScreenType
import app.skypub.navigation.SharedScreen
import app.skypub.ui.BottomNavigationBarMenu
import app.skypub.ui.DrawerContent
import app.skypub.ui.ModalNavigationDrawerWrapper
import app.skypub.ui.ScaffoldScreenContent
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ComposableImageRequest
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class NotificationScreen(
    private val profileUiState: ProfileUiState,
    private val screenType: ScreenType
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = koinInject<NotificationViewModel>()
        val uiState = viewModel.uiState.collectAsState().value
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val navigator = LocalNavigator.currentOrThrow
        val homeScreen = rememberScreen(SharedScreen.Home(profileUiState, ScreenType.HOME))
        val topScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        val bottomScrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()
        var selectedItem by remember { mutableIntStateOf(1) }
        val scope = rememberCoroutineScope()
        ModalNavigationDrawerWrapper(
            screenType = screenType,
            onMenuItemClick = { index ->
                when (index) {
                    0 -> navigator.push(homeScreen)
                    1 -> {}
                }
            },
            drawerContent = {
                DrawerContent(
                    avatar = profileUiState.avatar,
                    displayName = profileUiState.displayName,
                    handle = profileUiState.handle,
                    followersCount = profileUiState.followersCount,
                    followsCount = profileUiState.followsCount
                )
            },
            drawerState = drawerState
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        scrollBehavior = topScrollBehavior,
                        title = { Text("Notification") },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                AsyncImage(
                                    modifier = Modifier.clip(shape = CircleShape).fillMaxSize(0.7f),
                                    contentScale = ContentScale.Crop,
                                    request = ComposableImageRequest(profileUiState.avatar),
                                    contentDescription = ""
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                        )
                    )
                },
                bottomBar = {
                    BottomAppBar(
                        scrollBehavior = bottomScrollBehavior
                    ) {
                        BottomNavigationBarMenu.entries.forEachIndexed { index, item ->
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        imageVector = item.icon, contentDescription = ""
                                    )
                                },
                                label = { Text(item.label) },
                                selected = selectedItem == index,
                                onClick = {
                                    selectedItem = index
                                    when (item) {
                                        BottomNavigationBarMenu.Home -> {
                                            navigator.push(homeScreen)
                                        }

                                        BottomNavigationBarMenu.Notifications -> {}
                                    }
                                }
                            )
                        }
                    }
                }
            ) {
                ScaffoldScreenContent(
                    items = uiState.notifications,
                    content = { it ->
                        Text(text = it.name)
                    }
                )
            }
        }
    }
}