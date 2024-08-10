package app.skypub.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import app.skypub.post.PostScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.koin.compose.koinInject

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val viewmodel: HomeViewModel = koinInject<HomeViewModel>()
        var selectedItem by remember { mutableIntStateOf(0) }
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            bottomBar = {
                NavigationBar {
                    NavigationBarMenu.entries.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(imageVector = item.icon, contentDescription = "") },
                            label = { Text(item.label) },
                            selected = selectedItem == index,
                            onClick = { selectedItem = index }
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
            Column(modifier = Modifier.padding(it)) {
                Text(text = "Home")
                val feeds = viewmodel.feed.collectAsState()
                LazyColumn {
                    items(feeds.value) { feed ->
                        Text(text = feed.post.author.displayName)
                        Text(
                            text = feed.post.record.jsonObject["text"]?.jsonPrimitive?.content ?: ""
                        )
                    }
                }
            }
        }
    }
}

enum class NavigationBarMenu(val label: String, val icon: ImageVector) {
    Home("Home", Icons.Filled.Home),
    Notifications("Notifications", Icons.Filled.Notifications),
//    Messages("Messages", Icons.AutoMirrored.Filled.Message)
}