package com.example.rabbit.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class payloadRequest(
    @get:JsonProperty("number_one") var numberOne: Double? = null,
    @get:JsonProperty("number_two") var numberTwo: Double? = null
)


data class allSum(
    @get:JsonProperty("sum") var sum: Double? = null,
    @get:JsonProperty("minus") var minus: Double? = null,
    @get:JsonProperty("multiplication") var multiplication : Double? = null,
    @get:JsonProperty("division") var division: Double? = null,
)

enum class MathEnum(){
    SUM,
    MINUS,
    MULTIPLICATION,
    DIVISION
}