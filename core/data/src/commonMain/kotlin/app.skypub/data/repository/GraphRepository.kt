package app.skypub.data.repository

import app.skypub.network.model.CreateRecordInput
import app.skypub.network.model.CreateRecordResponse
import app.skypub.network.model.RequestErrorResponse
import arrow.core.Either

interface GraphRepository {
    suspend fun like(
        identifier: String,
        collection: String,
        limit: Int? = null,
        input: CreateRecordInput
    ): Either<RequestErrorResponse, CreateRecordResponse>
}