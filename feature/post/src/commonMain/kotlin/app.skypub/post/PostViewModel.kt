package app.skypub.post

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.skypub.data.repository.PostRepository
import app.skypub.network.model.CreateRecordInput
import arrow.core.Either
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class PostViewModel(
    private val postRepository: PostRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    private val _isSuccessCreatePost: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isSuccessCreatePostFlow = _isSuccessCreatePost.asStateFlow()

    fun createPost(text: String) {
        viewModelScope.launch {
            val identifier =
                dataStore.data.first()[stringPreferencesKey("identifier")] ?: return@launch
            when (val result = postRepository.createRecord(
                identifier = identifier,
                collection = "app.bsky.feed.post",
                input = CreateRecordInput(
                    text = text,
                    createdAt = Clock.System.now().toString()
                )
            )
            ) {
                is Either.Right -> {
                    _isSuccessCreatePost.value = true
                }

                is Either.Left -> {
                    _isSuccessCreatePost.value = false
                    Napier.e(tag = "PostViewModel") { "message: ${result.value.message}" }
                }
            }
        }
    }
}