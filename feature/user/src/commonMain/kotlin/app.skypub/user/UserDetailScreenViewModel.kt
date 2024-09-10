package app.skypub.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.skypub.common.ProfileUiState
import app.skypub.data.repository.PostRepository
import app.skypub.data.repository.UserRepository
import app.skypub.network.model.CreateRecordInput
import app.skypub.network.model.FeedItem
import app.skypub.network.model.GetProfileResponse
import app.skypub.network.model.Subject
import arrow.core.Either
import arrow.core.Either.Companion.zipOrAccumulate
import arrow.core.getOrElse
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class UserDetailScreenViewModel(
    private val userDetailScreenRepository: UserRepository,
    private val postRepository: PostRepository,
    private val dataStore: DataStore<Preferences>
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

    fun createRepost(
        subject: Subject,
        onSuccess: () -> Unit = {},
        onError: () -> Unit = {}
    ) = viewModelScope.launch {
        val identifier = dataStore.data.first()[stringPreferencesKey("identifier")] ?: return@launch
        when (val result = postRepository.createRecord(
            identifier = identifier,
            collection = "app.bsky.feed.repost",
            input = CreateRecordInput(
                createdAt = Clock.System.now().toString(),
                subject = subject
            )
        )
        ) {
            is Either.Right -> {
                onSuccess()
            }

            is Either.Left -> {
                onError()
                Napier.e(tag = "PostViewModel") { "message: ${result.value.message}" }
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