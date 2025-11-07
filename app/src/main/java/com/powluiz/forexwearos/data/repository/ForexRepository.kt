package com.powluiz.forexwearos.data.repository

import com.powluiz.forexwearos.data.api.RetrofitClient
import com.powluiz.forexwearos.data.model.CurrencyData
import com.powluiz.forexwearos.data.model.ForexDisplay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ForexRepository {

    private val api = RetrofitClient.forexApiService

    suspend fun getMainCurrencies(): Result<List<ForexDisplay>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getLatestRates("USD-BRL,EUR-BRL,GBP-BRL,BTC-BRL")

            val displayList = response.map { (key, data) ->
                mapToDisplay(data)
            }

            Result.success(displayList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun refreshExchangeRates(pairs: String): Result<List<ForexDisplay>> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getLatestRates(pairs)
                val displayList = response.map { (_, data) -> mapToDisplay(data) }
                Result.success(displayList)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    private fun mapToDisplay(data: CurrencyData): ForexDisplay {
        val pctChange = data.pctChange.toDoubleOrNull() ?: 0.0
        val isPositive = pctChange >= 0

        return ForexDisplay(
            pair = "${data.code}/${data.codein}",
            name = data.name,
            bidPrice = formatPrice(data.bid),
            variation = formatVariation(data.varBid),
            pctChange = formatPercentage(pctChange),
            isPositive = isPositive
        )
    }

    private fun formatPrice(price: String): String {
        val value = price.toDoubleOrNull() ?: return price
        return if (value >= 1000) {
            String.format("%.2f", value)
        } else {
            String.format("%.4f", value)
        }
    }

    private fun formatVariation(variation: String): String {
        val value = variation.toDoubleOrNull() ?: return variation
        val sign = if (value >= 0) "+" else ""
        return "$sign${String.format("%.2f", value)}"
    }

    private fun formatPercentage(pct: Double): String {
        val sign = if (pct >= 0) "+" else ""
        return "$sign${String.format("%.2f", pct)}%"
    }
}