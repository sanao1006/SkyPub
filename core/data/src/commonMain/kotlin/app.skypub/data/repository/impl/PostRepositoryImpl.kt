package app.skypub.data.repository.impl

import app.skypub.data.repository.PostRepository
import app.skypub.network.BlueskyApiDataSource
import app.skypub.network.model.CreateRecordInput
import app.skypub.network.model.CreateRecordResponse
import app.skypub.network.model.RequestErrorResponse
import arrow.core.Either

class PostRepositoryImpl(
    private val blueskyApiDataSource: BlueskyApiDataSource
) : PostRepository {
    override suspend fun createRecord(
        identifier: String,
        collection: String,
        rkey: String,
        validate: Boolean,
        input: CreateRecordInput
    ): Either<RequestErrorResponse, CreateRecordResponse> =
        blueskyApiDataSource.createRecord(identifier, collection, rkey, validate, input)
}
