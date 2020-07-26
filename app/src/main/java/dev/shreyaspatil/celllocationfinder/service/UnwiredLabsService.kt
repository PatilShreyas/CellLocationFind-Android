package dev.shreyaspatil.celllocationfinder.service

import dev.shreyaspatil.celllocationfinder.model.request.CellInfo
import dev.shreyaspatil.celllocationfinder.model.response.CellLocation
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UnwiredLabsService {

    @POST("v2/process.php")
    suspend fun getLocationByCellInfo(@Body cellInfo: CellInfo): Response<CellLocation>

    companion object {
        const val BASE_URL = "https://ap1.unwiredlabs.com/"
    }
}