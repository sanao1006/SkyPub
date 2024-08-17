package app.skypub.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.skypub.data.repository.GraphRepository
import app.skypub.data.repository.HomeRepository
import app.skypub.network.model.CreateRecordInput
import app.skypub.network.model.FeedItem
import app.skypub.network.model.Subject
import arrow.core.Either
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class HomeViewModel(
    private val homeRepository: HomeRepository,
    private val graphRepository: GraphRepository
) : ViewModel() {
    private val _feed = MutableStateFlow(emptyList<FeedItem>())
    val feed: StateFlow<List<FeedItem>>
        get() = _feed.asStateFlow()

    fun loadFeed() {
        viewModelScope.launch {
            homeRepository.getTimeLine().catch { e ->
                Napier.e(tag = "loadFeedError") { "message: ${e.message}" }
            }.collectLatest {
                _feed.value = it.feed
            }
        }
    }

    fun like(identifier: String, uri: String, cid: String) {
        viewModelScope.launch {
            val request = graphRepository.like(
                identifier = identifier,
                collection = "app.bsky.feed.like",
                input = CreateRecordInput(
                    createdAt = Clock.System.now().toString(),
                    subject = Subject(
                        uri = uri,
                        cid = cid
                    )
                )
            )
            when (request) {
                is Either.Right -> {}
                is Either.Left -> {
                    Napier.e(tag = "likeError") {
                        "message: ${request.value.message}"
                    }
                }
            }
        }
    }
}
