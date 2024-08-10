package app.skypub.data.repository

import app.skypub.network.model.GetTimeLineResponse
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getTimeLine(
        algorithm: String? = null,
        limit: Int? = null,
        cursor: String? = null
    ): Flow<GetTimeLineResponse>
}
