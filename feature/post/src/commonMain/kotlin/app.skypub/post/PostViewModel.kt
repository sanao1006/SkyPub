package app.skypub.post

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.skypub.data.repository.PostRepository
import app.skypub.network.model.CreateRecordInput
import app.skypub.network.model.ReplyRef
import arrow.core.Either
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class PostViewModel(
    private val postRepository: PostRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    fun createPost(
        text: String,
        onSuccess: () -> Unit = {},
        onError: () -> Unit = {}
    ) {
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
                    onSuccess()
                }

                is Either.Left -> {
                    onError()
                    Napier.e(tag = "PostViewModel") { "message: ${result.value.message}" }
                }
            }
        }
    }

    fun createReply(
        text: String,
        ref: ReplyRef,
        onSuccess: () -> Unit = {},
        onError: () -> Unit = {}
    ) {
        viewModelScope.launch {
            val identifier =
                dataStore.data.first()[stringPreferencesKey("identifier")] ?: return@launch
            when (val result = postRepository.createRecord(
                identifier = identifier,
                collection = "app.bsky.feed.post",
                input = CreateRecordInput(
                    text = text,
                    createdAt = Clock.System.now().toString(),
                    reply = ref
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

}