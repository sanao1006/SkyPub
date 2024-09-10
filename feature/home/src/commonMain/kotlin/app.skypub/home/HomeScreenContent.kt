package app.skypub.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.skypub.composables.ContentIcons
import app.skypub.composables.TimeLinePostItem
import app.skypub.network.model.FeedItem
import cafe.adriel.voyager.navigator.Navigator

@Composable
fun HomeScreenContent(
    feeds: List<FeedItem>,
    navigator: Navigator,
    modifier: Modifier = Modifier,
    onIconClick: (ContentIcons, String, String, String) -> Unit,
    onRepostIconClick: (String, String) -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (feeds.isEmpty()) {
            CircularProgressIndicator()
        } else {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                itemsIndexed(feeds) { index, feed ->
                    if (index != 0) {
                        HorizontalDivider()
                    }
                    TimeLinePostItem(
                        navigator = navigator,
                        feed = feed,
                        modifier = Modifier
                            .padding(top = 12.dp),
                        onIconClick = onIconClick,
                        onRepostIconClick = { uri, cid -> onRepostIconClick(uri, cid) }
                    )
                }
            }
        }
    }
}

