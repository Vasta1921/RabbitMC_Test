package com.example.rabbit.amqp

import com.example.rabbit.dto.payloadRequest
import com.example.rabbit.dto.allSum
import com.example.rabbit.service.MathService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service


@Service
class RabbitListner(
    private val mathService: MathService) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @RabbitListener(queues = [RabbitConfig.QUERY])
    fun getChannelsInfo(@Payload  request : ExchangeData<payloadRequest>): ExchangeData<allSum?> {
        return mathService.getAllSum(request)
    }

}