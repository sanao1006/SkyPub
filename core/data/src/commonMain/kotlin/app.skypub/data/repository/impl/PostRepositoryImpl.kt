package app.skypub.data.repository.impl

import app.skypub.data.repository.PostRepository
import app.skypub.network.model.CreateRecordInput
import app.skypub.network.model.CreateRecordResponse
import app.skypub.network.model.RequestErrorResponse
import arrow.core.Either

class PostRepositoryImpl(
    private val postRepository: PostRepository
) : PostRepository {
    override suspend fun createRecord(
        identifier: String,
        collection: String,
        rkey: String,
        validate: Boolean,
        input: CreateRecordInput
    ): Either<RequestErrorResponse, CreateRecordResponse> =
        postRepository.createRecord(identifier, collection, rkey, validate, input)
}
