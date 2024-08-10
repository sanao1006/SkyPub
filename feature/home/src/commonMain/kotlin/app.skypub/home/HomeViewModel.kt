package app.skypub.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.skypub.data.repository.HomeRepository
import app.skypub.network.model.FeedItem
import arrow.core.Either
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

    private val _profileUiState = MutableStateFlow(ProfileUiState("", "", "", 0, 0))
    val profileUiState: StateFlow<ProfileUiState>
        get() = _profileUiState.asStateFlow()

    private fun loadFeed() {
        viewModelScope.launch {
            homeRepository.getTimeLine().catch { e ->
                Napier.e(tag = "loadFeedError") { "message: ${e.message}" }
            }.collectLatest {
                _feed.value = it.feed
            }
        }
    }

    fun getProfile() {
        viewModelScope.launch {
            when (val result = homeRepository.getProfile()) {
                is Either.Right -> {
                    _profileUiState.value = ProfileUiState(
                        avatar = result.value.avatar,
                        handle = result.value.handle,
                        displayName = result.value.displayName,
                        followsCount = result.value.followsCount,
                        followersCount = result.value.followersCount
                    )
                }

                is Either.Left -> {
                    Napier.e(tag = "getProfileError") {
                        "message: ${result.value.message}"
                    }
                }
            }
        }
    }

    init {
        loadFeed()
    }
}

data class ProfileUiState(
    val avatar: String,
    val handle: String,
    val displayName: String,
    val followsCount: Int,
    val followersCount: Int,
)