package app.skypub.data.repository.impl

import app.skypub.data.repository.GraphRepository
import app.skypub.network.BlueskyApiDataSource
import app.skypub.network.model.CreateRecordInput
import app.skypub.network.model.CreateRecordResponse
import app.skypub.network.model.RequestErrorResponse
import arrow.core.Either

class GraphRepositoryImpl(
    private val blueskyApiDataSource: BlueskyApiDataSource
) : GraphRepository {
    override suspend fun like(
        identifier: String,
        collection: String,
        limit: Int?,
        input: CreateRecordInput
    ): Either<RequestErrorResponse, CreateRecordResponse> {
        return blueskyApiDataSource.createRecord(
            identifier = identifier,
            collection = "app.bsky.feed.like",
            input = input
        )
    }
}