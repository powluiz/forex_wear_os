package com.powluiz.forexwearos.data.model

import com.google.gson.annotations.SerializedName

data class CurrencyData(
    @SerializedName("code")
    val code: String,

    @SerializedName("codein")
    val codein: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("bid")
    val bid: String,

    @SerializedName("varBid")
    val varBid: String,

    @SerializedName("ask")
    val ask: String,

    @SerializedName("pctChange")
    val pctChange: String,

    @SerializedName("high")
    val high: String,

    @SerializedName("low")
    val low: String,

    @SerializedName("timestamp")
    val timestamp: String
)

data class ForexDisplay(
    val pair: String,
    val name: String,
    val bidPrice: String,
    val variation: String,
    val pctChange: String,
    val isPositive: Boolean
)