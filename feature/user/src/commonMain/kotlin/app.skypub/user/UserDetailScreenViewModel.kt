package app.skypub.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.skypub.common.ProfileUiState
import app.skypub.data.repository.UserRepository
import app.skypub.network.model.FeedItem
import app.skypub.network.model.GetProfileResponse
import arrow.core.Either.Companion.zipOrAccumulate
import arrow.core.getOrElse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserDetailScreenViewModel(
    private val userDetailScreenRepository: UserRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UserDetailScreenState> =
        MutableStateFlow(UserDetailScreenState.Loading)
    val uiState = _uiState.asStateFlow()

    fun getUiState(handle: String) {
        viewModelScope.launch {
            zipOrAccumulate(
                userDetailScreenRepository.getProfile(handle),
                userDetailScreenRepository.getAuthorFeed(handle)
            ) { profile, feeds ->
                _uiState.value = UserDetailScreenState.UserDetailScreenUiState(
                    profile = profile.toProfileUiState(),
                    feeds = feeds.feed
                )
            }.getOrElse {
                _uiState.value = UserDetailScreenState.Error(it.first().message)
            }
        }
    }
}

sealed class UserDetailScreenState {
    data object Loading : UserDetailScreenState()

    data class Error(val message: String) : UserDetailScreenState()

    data class UserDetailScreenUiState(
        val profile: ProfileUiState,
        val feeds: List<FeedItem>
    ) : UserDetailScreenState() {
        fun isLoading(): Boolean {
            return profile == ProfileUiState() && feeds.isEmpty()
        }
    }
}

fun GetProfileResponse.toProfileUiState(): ProfileUiState {
    return ProfileUiState(
        avatar = avatar,
        handle = handle,
        displayName = displayName,
        followsCount = followsCount,
        followersCount = followersCount
    )
}