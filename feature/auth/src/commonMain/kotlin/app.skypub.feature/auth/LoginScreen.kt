package app.skypub.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import app.skypub.common.ScreenType
import app.skypub.home.HomeScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.koinInject


data class LoginScreen(val platform: Platform) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator: Navigator = LocalNavigator.currentOrThrow
        val viewModel: AuthViewModel = koinInject<AuthViewModel>()
        val hostState = remember { SnackbarHostState() }
        val profileUiState = viewModel.profileUiState.collectAsState()
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Login ${platform.value}") },
                    navigationIcon = {
                        IconButton(onClick = navigator::pop) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = ""
                            )
                        }
                    }
                )
            },
            snackbarHost = { SnackbarHost(hostState = hostState) }
        ) {
            Column(
                modifier = Modifier.padding(it).padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (platform) {
                    Platform.Bluesky -> BlueSkyLoginScreen(
                        onClick = { identifier, password ->
                            viewModel.login(
                                identifier,
                                password,
                                onSuccess = {
                                    navigator.popUntilRoot()
                                    navigator.replace(
                                        HomeScreen(
                                            profileUiState.value,
                                            ScreenType.HOME
                                        )
                                    )
                                },
                                onFailure = { hostState.showSnackbar("Log in Failed...") }
                            )
                        }
                    )

                    Platform.Misskey -> MisskeyLoginScreen()
                }
            }
        }
    }
}

@Composable
fun BlueSkyLoginScreen(
    onClick: (String, String) -> Unit = { identifier, password -> }
) {
    var userName by rememberSaveable { mutableStateOf("") }
    var passWord by rememberSaveable { mutableStateOf("") }
    Column(horizontalAlignment = Alignment.Start) {
        Text("account", modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(4.dp))
        val focusManager = LocalFocusManager.current
        TextField(
            modifier = Modifier.onPreviewKeyEvent {
                if (it.key == Key.Tab) {
                    focusManager.moveFocus(FocusDirection.Down)
                    true
                } else {
                    false
                }
            },
            value = userName,
            onValueChange = { userName = it },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AlternateEmail,
                    contentDescription = ""
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            placeholder = { Text("email or user name") }
        )
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            modifier = Modifier.onPreviewKeyEvent {
                if (it.key == Key.Tab) {
                    focusManager.moveFocus(FocusDirection.Down)
                    true
                } else {
                    false
                }
            },
            value = passWord,
            onValueChange = { passWord = it },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, "") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            placeholder = { Text("password") }
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Button(
        onClick = { onClick(userName, passWord) },
        enabled = userName.isNotBlank() && passWord.isNotBlank(),
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
    ) {
        Text("Login")
    }
}

@Composable
fun MisskeyLoginScreen() {
    Text("Misskey")
}