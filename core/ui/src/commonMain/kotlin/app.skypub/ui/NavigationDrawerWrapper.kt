package app.skypub.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.skypub.common.ScreenType
import app.skypub.data.repository.UserRepository
import arrow.core.Either
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.rememberScreen
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ComposableImageRequest
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ModalNavigationDrawerWrapper(
    screenType: ScreenType,
    drawerState: DrawerState,
    drawerContent: @Composable () -> Unit = {},
    onMenuItemClick: (NavigationDrawerMainMenu) -> Unit = {},
    modifier: Modifier = Modifier,
    gesturesEnabled: Boolean = true,
    scrimColor: Color = DrawerDefaults.scrimColor,
    snackbarHostState: SnackbarHostState,
    navigator: cafe.adriel.voyager.navigator.Navigator,
    viewModel: NavDrawerViewModel = koinViewModel<NavDrawerViewModel>(),
    content: @Composable () -> Unit
) {
    var selectedItemIndex by rememberSaveable { mutableStateOf(screenType.index) }
    val scope = rememberCoroutineScope()
    var isDialogShow by rememberSaveable { mutableStateOf(false) }
    val loginScreen = rememberScreen(LoginScreen.Login)
    if (isDialogShow) {
        LogOutConfirmDialog(
            onConfirm = {
                scope.launch {
                    viewModel.logout(
                        onFailed = {
                            snackbarHostState.showSnackbar("Logout failed")
                        },
                        onSuccess = {
                            navigator.popUntilRoot()
                            navigator.replace(loginScreen)
                        }
                    )
                }
                isDialogShow = false
            },
            onDismissRequest = {
                isDialogShow = false
            }
        )
    }
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(modifier = modifier) {
                Spacer(modifier = Modifier.height(24.dp))
                drawerContent()
                Spacer(modifier = Modifier.height(24.dp))
                NavigationDrawerMainMenu.entries.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        label = { Text(item.label) },
                        onClick = {
                            selectedItemIndex = index
                            scope.launch {
                                drawerState.close()
                                onMenuItemClick(item)
                            }
                        },
                        selected = index == screenType.index,
                        icon = { Icon(imageVector = item.imageVector, contentDescription = "") }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))
                AccountMenu.entries.forEachIndexed { index, accountMenu ->
                    NavigationDrawerItem(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        label = { Text(accountMenu.label) },
                        onClick = {
                            scope.launch {
                                when (accountMenu) {
                                    AccountMenu.SETTINGS -> {

                                    }

                                    AccountMenu.LOGOUT -> {
                                        isDialogShow = true
                                    }
                                }
                                drawerState.close()
                            }
                        },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = accountMenu.imageVector,
                                contentDescription = ""
                            )
                        }
                    )
                }
            }
        },
        modifier = modifier,
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        scrimColor = scrimColor,
        content = content
    )
}

@Composable
fun DrawerContent(
    avatar: String,
    displayName: String,
    handle: String,
    followersCount: Int,
    followsCount: Int,
    modifier: Modifier = Modifier,
    onAvatarClick: () -> Unit = {}
) {
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        IconButton(onClick = onAvatarClick) {
            AsyncImage(
                modifier = Modifier.clip(shape = CircleShape).fillMaxSize(1.2f),
                contentScale = ContentScale.Crop,
                request = ComposableImageRequest(avatar),
                contentDescription = ""
            )
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = displayName,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(text = "@${handle}")
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text(text = "Followers: $followersCount")
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = "Follows: $followsCount")
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
    }
}

sealed interface NavMenu

enum class NavigationDrawerMainMenu(
    val imageVector: ImageVector,
    val label: String
) :
    NavMenu {
    HOME(Icons.Default.Home, "Home"),
    NOTIFICATIONS(Icons.Default.Notifications, "Notifications"),
}

enum class AccountMenu(val imageVector: ImageVector, val label: String) : NavMenu {
    SETTINGS(Icons.Default.Settings, "Settings"),
    LOGOUT(Icons.AutoMirrored.Filled.Logout, "Logout"),
}

class NavDrawerViewModel(
    private val userRepository: UserRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    suspend fun logout(
        onFailed: suspend () -> Unit,
        onSuccess: suspend () -> Unit
    ) {
        viewModelScope.launch {
            when (userRepository.deleteSession()) {
                is Either.Left -> {
                    onFailed()
                }

                is Either.Right -> {
                    dataStore.edit {
                        it.remove(stringPreferencesKey("access_jwt"))
                        it.remove(stringPreferencesKey("refresh_jwt"))
                        it.remove(stringPreferencesKey("identifier"))
                    }
                    onSuccess()
                }
            }
        }
    }
}

sealed class LoginScreen : ScreenProvider {
    data object Login : LoginScreen()
}