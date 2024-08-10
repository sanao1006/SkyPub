package app.skypub.network.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateRecordResponse(
    val uri: String,
    val cid: String,
)

@Serializable
data class CreateRecordRequestBody(
    val repo: String,
    val collection: String,
    val rkey: String? = null,
    val validate: Boolean,
    val record: CreateRecordInput
)

@Serializable
data class CreateRecordInput(
    val text: String,
    val createdAt: String,
    val langs: List<String>? = null,
)