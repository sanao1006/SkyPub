package app.skypub.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import app.skypub.composables.RepostBottomSheet
import app.skypub.composables.RepostItem
import app.skypub.composables.TimeLinePostItem
import app.skypub.network.model.Subject
import app.skypub.ui.ScaffoldScreenContent
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class UserDetailScreen(private val handle: String) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewmodel = koinInject<UserDetailScreenViewModel>()
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        val state = viewmodel.uiState.collectAsState().value
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        var openRepostModal by remember { mutableStateOf(false) }
        var repostParam by rememberSaveable { mutableStateOf("" to "") }
        LaunchedEffect(Unit) {
            viewmodel.getUiState(handle)
        }
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {},
                    scrollBehavior = scrollBehavior,
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, "")
                        }
                    }
                )
            }
        ) {
            Column(modifier = Modifier.padding(it)) {
                when (state) {
                    is UserDetailScreenState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is UserDetailScreenState.Error -> {
                        scope.launch {
                            snackbarHostState.showSnackbar("Loading failed")
                        }
                    }

                    is UserDetailScreenState.UserDetailScreenUiState -> {
                        ScaffoldScreenContent(
                            items = state.feeds,
                        ) { feed ->
                            TimeLinePostItem(
                                navigator = navigator,
                                feed = feed,
                                modifier = Modifier.padding(top = 8.dp, bottom = 2.dp),
                                onIconClick = { _, _, _, _ -> },
                                onRepostIconClick = { uid, cid ->
                                    repostParam = uid to cid
                                    openRepostModal = true
                                }
                            )
                        }
                    }
                }
            }
        }
        if (openRepostModal) {
            RepostBottomSheet(
                onDismiss = { openRepostModal = it },
                onclick = {
                    when (it) {
                        RepostItem.REPOST -> {
                            viewmodel.createRepost(
                                subject = Subject(
                                    uri = repostParam.first,
                                    cid = repostParam.second
                                )
                            )
                        }
                    }
                }
            )
        }
    }
}