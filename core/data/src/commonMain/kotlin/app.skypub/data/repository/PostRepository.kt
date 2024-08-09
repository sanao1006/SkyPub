package app.skypub.data.repository

import app.skypub.network.model.CreateRecordInput
import app.skypub.network.model.CreateRecordResponse
import app.skypub.network.model.RequestErrorResponse
import arrow.core.Either

interface PostRepository {
    suspend fun createRecord(
        identifier: String,
        collection: String,
        rkey: String = "<= 15 characters",
        validate: Boolean = true,
        input: CreateRecordInput
    ): Either<RequestErrorResponse, CreateRecordResponse>
}