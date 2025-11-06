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

    @SerializedName("ask")
    val ask: String,

    @SerializedName("pctChange")
    val percentChange: String,

    @SerializedName("high")
    val high: String,

    @SerializedName("low")
    val low: String,

    @SerializedName("timestamp")
    val timestamp: String
)