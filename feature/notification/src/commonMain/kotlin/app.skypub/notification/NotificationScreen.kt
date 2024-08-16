package app.skypub.notification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Reply
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Repeat
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.skypub.common.ProfileUiState
import app.skypub.common.ReasonEnum
import app.skypub.common.ScreenType
import app.skypub.navigation.SharedScreen
import app.skypub.navigation.UserScreen
import app.skypub.network.model.NotificationDomainModel
import app.skypub.ui.BottomNavigationBarMenu
import app.skypub.ui.DrawerContent
import app.skypub.ui.ModalNavigationDrawerWrapper
import app.skypub.ui.ScaffoldScreenContent
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ComposableImageRequest
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
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
        var selectedItem by remember { mutableIntStateOf(ScreenType.NOTIFICATION.index) }
        val homeScreen =
            rememberScreen(SharedScreen.Home(profileUiState, ScreenType.getType(selectedItem)))
        val topScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        val bottomScrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()
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
                modifier = Modifier
                    .nestedScroll(bottomScrollBehavior.nestedScrollConnection)
                    .nestedScroll(topScrollBehavior.nestedScrollConnection),
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
                    modifier = Modifier.padding(it),
                    items = uiState.notifications,
                    content = { it ->
                        NotificationItem(
                            notification = it,
                            navigator = navigator,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun NotificationItem(
    notification: NotificationDomainModel,
    navigator: Navigator,
    modifier: Modifier = Modifier
) {
    val userDetailScreen = rememberScreen(UserScreen.UserDetail(notification.handle))
    Row(modifier) {
        NotificationIcon(reason = ReasonEnum.getType(notification.reason))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            AsyncImage(
                modifier = Modifier.size(40.dp).clip(CircleShape)
                    .clickable(onClick = { navigator.push(userDetailScreen) }),
                contentScale = ContentScale.Crop,
                request = ComposableImageRequest(notification.avatar),
                contentDescription = ""
            )
            val text = when (ReasonEnum.getType(notification.reason)) {
                ReasonEnum.LIKE -> "liked your post"
                ReasonEnum.REPLY -> "replied to your post"
                ReasonEnum.MENTION -> "mentioned you in a post"
                ReasonEnum.REPOST -> "reposted your post"
                ReasonEnum.QUOTE -> "quoted your post"
                ReasonEnum.FOLLOW -> "followed you"
                ReasonEnum.STARTERPACK_JOINED -> "joined the starter pack"
                ReasonEnum.UNKNOWN -> ""
            }
            Text(
                text = "${notification.name}: $text",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = notification.record?.jsonObject?.get("text")?.jsonPrimitive?.content ?: "",
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 16.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            val localDate = Instant.parse(notification.createdAt)
                .toLocalDateTime(TimeZone.currentSystemDefault())
            Text(
                // TODO FIXME: This is not the correct format
                text = "${localDate.year}/${localDate.monthNumber}/${localDate.dayOfMonth}",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun NotificationIcon(
    reason: ReasonEnum,
    modifier: Modifier = Modifier
) {
    val imageVector = when (reason) {
        ReasonEnum.LIKE -> Icons.Outlined.Favorite
        ReasonEnum.REPLY, ReasonEnum.MENTION -> Icons.AutoMirrored.Outlined.Reply
        ReasonEnum.REPOST, ReasonEnum.QUOTE -> Icons.Outlined.Repeat
        ReasonEnum.FOLLOW -> Icons.Outlined.PersonAdd
        ReasonEnum.STARTERPACK_JOINED, ReasonEnum.UNKNOWN -> null
    }
    imageVector?.let {
        Icon(
            modifier = modifier.padding(top = 3.dp),
            imageVector = it,
            contentDescription = ""
        )
    }
}