package com.powluiz.forexwearos.data.api

import com.powluiz.forexwearos.data.model.CurrencyData
import retrofit2.http.GET
import retrofit2.http.Path

interface ForexApiService {

    @GET("json/last/{currencies}")
    suspend fun getLatestRates(
        @Path("currencies") currencies: String
    ): Map<String, CurrencyData>
}