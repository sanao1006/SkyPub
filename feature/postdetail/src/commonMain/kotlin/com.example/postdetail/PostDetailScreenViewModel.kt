package com.example.postdetail

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.skypub.data.repository.PostRepository
import app.skypub.network.model.CreateRecordInput
import app.skypub.network.model.Subject
import arrow.core.Either
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class PostDetailScreenViewModel(
    private val postRepository: PostRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
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