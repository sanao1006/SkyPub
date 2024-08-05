package app.skypub.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.skypub.data.repository.FeedRepository
import app.skypub.network.model.FeedItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val feedRepository: FeedRepository
) : ViewModel() {
    private val _feed = MutableStateFlow(emptyList<FeedItem>())
    val feed: StateFlow<List<FeedItem>>
        get() = _feed.asStateFlow()

    fun loadFeed() {
        viewModelScope.launch {
            feedRepository.getTimeLine().collectLatest { response ->
                _feed.value = response.feed
            }
        }
    }

    init {
        loadFeed()
    }
}
