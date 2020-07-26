package dev.shreyaspatil.celllocationfinder.repository

import dev.shreyaspatil.celllocationfinder.model.request.CellInfo
import dev.shreyaspatil.celllocationfinder.service.UnwiredLabsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class OpenCellRepository(
    private val service: UnwiredLabsService
) {
    suspend fun getLocationByCellInfo(cellInfo: CellInfo) = flow {
        val response = service.getLocationByCellInfo(cellInfo)
        emit(response.body())
    }.flowOn(Dispatchers.IO)
}
