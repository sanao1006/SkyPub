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
    val createdAt: String,
    val text: String? = null,
    val langs: List<String>? = null,
    val subject: Subject? = null,
)

@Serializable
data class Subject(
    val uri: String,
    val cid: String
)