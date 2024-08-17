package app.skypub.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import app.skypub.common.ProfileUiState
import app.skypub.common.ScreenType
import app.skypub.navigation.SharedScreen
import app.skypub.navigation.UserScreen
import app.skypub.post.PostScreen
import app.skypub.ui.BottomNavigationBarMenu
import app.skypub.ui.DrawerContent
import app.skypub.ui.ModalNavigationDrawerWrapper
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ComposableImageRequest
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class HomeScreen(
    private val profileUiState: ProfileUiState,
    private val screenType: ScreenType
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewmodel: HomeViewModel = koinInject<HomeViewModel>()
        var selectedItem by remember { mutableIntStateOf(ScreenType.HOME.index) }
        val navigator = LocalNavigator.currentOrThrow
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val bottomScrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()
        val topScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        val feeds = viewmodel.feed.collectAsState().value
        val notificationScreen =
            rememberScreen(
                SharedScreen.Notification(
                    profileUiState = profileUiState,
                    screenType = ScreenType.getType(selectedItem)
                )
            )
        val myProfileScreen = rememberScreen(
            UserScreen.UserDetail(
                handle = profileUiState.handle
            )
        )
        LaunchedEffect(feeds) {
            viewmodel.loadFeed()
        }
        ModalNavigationDrawerWrapper(
            screenType = screenType,
            onMenuItemClick = { index ->
                when (index) {
                    0 -> {}
                    1 -> {
                        navigator.push(notificationScreen)
                    }
                }
            },
            drawerContent = {
                DrawerContent(
                    avatar = profileUiState.avatar,
                    displayName = profileUiState.displayName,
                    handle = profileUiState.handle,
                    followersCount = profileUiState.followersCount,
                    followsCount = profileUiState.followsCount,
                    onAvatarClick = {
                        scope.launch {
                            drawerState.close()
                            navigator.push(myProfileScreen)
                        }
                    }
                )
            },
            drawerState = drawerState
        ) {
            Scaffold(
                modifier = Modifier
                    .nestedScroll(bottomScrollBehavior.nestedScrollConnection)
                    .nestedScroll(topScrollBehavior.nestedScrollConnection),
                topBar = {
                    TopAppBar(
                        scrollBehavior = topScrollBehavior,
                        title = { Text("Home") },
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
                                        BottomNavigationBarMenu.Home -> {}
                                        BottomNavigationBarMenu.Notifications -> navigator.push(
                                            notificationScreen
                                        )
                                    }
                                }
                            )
                        }
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = { navigator.push(PostScreen()) }) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = "")
                    }
                }
            ) {
                HomeScreenContent(
                    feeds = feeds,
                    navigator = navigator,
                    modifier = Modifier
                        .padding(it)
                        .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
                        .fillMaxSize(),
                    onIconClick = { icon, identifier, uri, cid ->
                        when (icon) {
                            ContentIcons.FavoriteBorder -> viewmodel.like(identifier, uri, cid)
                            else -> {}
                        }
                    }
                )
            }
        }
    }
}
