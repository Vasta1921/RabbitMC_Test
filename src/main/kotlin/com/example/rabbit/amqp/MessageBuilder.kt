package com.example.rabbit.amqp

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.stereotype.Service
import java.util.*

@Service
class MessageBuilder {

    fun <K, T> makeResponse(request: ExchangeData<K>, data: T? = null,error: String? = null) = ExchangeData(
        messageId = request.messageId,
        requestId = request.requestId,
        messageType = "response",
        messageSource = "rabbit",
        data = data,
        error = error
    )
}

open class ExchangeData<T>(
    @get:JsonProperty("message_id")
    val messageId: String? = null,
    @get:JsonProperty("request_id")
    val requestId: String? = null,
    @get:JsonProperty("message_source")
    val messageSource: String? = null,
    @get:JsonProperty("message_type")
    val messageType: String? = null,
    @get:JsonProperty("data")
    val data: T? = null,
    @get:JsonProperty("error")
    val error: String? = null
) {
    override fun toString(): String {
        return "messageId=$messageId, data=$data"
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonInclude(JsonInclude.Include.NON_NULL)
enum class ErrorResponse(
    @get:JsonProperty("title") val title: String? = null,
    @get:JsonProperty("message") val message: String? = null,
    @get:JsonProperty("code") val code: String? = null,
    @get:JsonProperty("request_id") val requestId: String? = null
) {
    NOT_FOUND_ADDRESS_REST(
        "Адрес не найден",
        "Адрес по переданному идентификатору не найден",
        "address_not_found",
        UUID.randomUUID().toString()
    ),
    NOT_FOUND_ADDRESS_RABBIT(code = "address_not_found")
}