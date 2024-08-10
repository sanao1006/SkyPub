package app.skypub.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.skypub.data.repository.HomeRepository
import app.skypub.network.model.FeedItem
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepository: HomeRepository
) : ViewModel() {
    private val _feed = MutableStateFlow(emptyList<FeedItem>())
    val feed: StateFlow<List<FeedItem>>
        get() = _feed.asStateFlow()

    private fun loadFeed() {
        viewModelScope.launch {
            homeRepository.getTimeLine().catch { e ->
                Napier.e(tag = "loadFeedError") { "message: ${e.message}" }
            }.collectLatest {
                _feed.value = it.feed
            }
        }
    }

    init {
        loadFeed()
    }
}
